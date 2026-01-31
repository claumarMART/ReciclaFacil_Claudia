package com.juandeherrera.reciclafacil.localdb

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

// objeto singleton que crea la base de datos, devuelve la misma instancia e inserta datos iniciales por primera vez
object DatabaseProvider {

    // instancia de la base de datos
    @Volatile
    private var INSTANCE: AppDB? = null

    /*
    devuelve la instancia de la base de datos

    si la base de datos no existe: se crea y se inserta los datos iniciales

    si la base de datos existe: solo devuelve la instancia de esta
    */
    fun getDatabase(context: Context): AppDB {

        // si la instancia existe, se devuelve directamente
        return INSTANCE ?: synchronized(this) {

            // si no existe la instancia, se crea de manera segura
            val instance = Room.databaseBuilder(
                context.applicationContext,  // contexto global de la aplicacion
                AppDB::class.java,   // clase de la base de datos
                Estructura.DB.NAME          // nombre del archivo SQLite
            )
                // callback que escucha eventos en la base de datos
                .addCallback(object: RoomDatabase.Callback(){

                    // se ejecuta una sola vez cuando la base de datos se crea por primera vez
                    override fun onCreate(db: SupportSQLiteDatabase) {

                        super.onCreate(db)

                        // se lanza una corrutina en segundo plano ya que Room no permite hacerlo en el hilo principal
                        CoroutineScope(Dispatchers.IO).launch {

                            // insertamos los productos en la base de datos
                            INSTANCE?.productoDao()?.insertarProductos(
                                listOf(
                                    ProductoData(
                                        tituloProducto = "Pilas",
                                        descripcionProducto = "Las pilas y baterías contienen metales pesados y sustancias químicas que pueden ser muy contaminantes si se tiran a la basura común. " +
                                                "Nunca deben desecharse en los contenedores habituales. Para reciclarlas correctamente, deposítalas en los puntos específicos para pilas, que suelen " +
                                                "encontrarse en supermercados, colegios o puntos limpios. Allí se gestionan de manera segura para recuperar materiales y reducir el impacto " +
                                                "ambiental, evitando la contaminación del suelo y del agua.",
                                        contenedorProducto = "Contenedor de pilas"
                                    ),
                                    ProductoData(
                                        tituloProducto = "Botellas de plástico",
                                        descripcionProducto = "Las botellas de plástico incluyen envases de agua, refrescos, detergentes y otros líquidos. Antes de tirarlas, " +
                                                "vacíalas completamente y aplástalas si es posible para ahorrar espacio en el contenedor. No es necesario retirar las tapas, " +
                                                "pero si quieres, puedes reciclarlas por separado según la normativa local. Estas botellas se depositan en el contenedor " +
                                                "amarillo, junto con otros envases de plástico y latas, donde se reciclan para fabricar nuevos envases, fibras textiles u " +
                                                "otros productos de plástico, ayudando a reducir la contaminación y el consumo de recursos naturales.",
                                        contenedorProducto = "Contenedor amarillo"
                                    ),
                                    ProductoData(
                                        tituloProducto = "Papel y cartón",
                                        descripcionProducto = "El papel y el cartón incluyen periódicos, revistas, folletos, cajas, envases de cartón limpios y cualquier " +
                                                "tipo de papel de oficina o embalaje. Antes de depositarlos, asegúrate de que estén limpios, secos y libres de restos de " +
                                                "comida o plásticos, ya que esto facilita su reciclaje y evita que contaminen otros materiales. Dóblalos o aplástalos si " +
                                                "ocupan mucho espacio. Estos materiales se depositan en el contenedor azul, donde se reciclan para fabricar nuevo papel y " +
                                                "cartón, contribuyendo a la conservación de árboles y a la reducción de residuos.",
                                        contenedorProducto = "Contenedor azul"
                                    ),
                                    ProductoData(
                                        tituloProducto = "Residuos orgánicos",
                                        descripcionProducto = "Los residuos orgánicos incluyen restos de comida, cáscaras de frutas y verduras, restos de café y té, " +
                                                "restos de pan, huesos pequeños, restos de jardín como hojas, flores y césped. Estos residuos deben separarse de " +
                                                "plásticos, envases o materiales no biodegradables para facilitar su transformación en compost y fertilizante " +
                                                "natural, ayudando a reducir la cantidad de residuos que terminan en vertederos y fomentando un ciclo sostenible de nutrientes.",
                                        contenedorProducto = "Contenedor marrón"
                                    ),
                                    ProductoData(
                                        tituloProducto = "Tetrabricks",
                                        descripcionProducto = "Los tetrabricks son envases de cartón recubiertos de plástico y aluminio, utilizados habitualmente para leche, " +
                                                "zumos, sopas y otros líquidos. Antes de tirarlos al contenedor, es recomendable vaciarlos completamente y, si es posible, " +
                                                "enjuagarlos ligeramente para evitar malos olores y contaminación de otros residuos. Estos envases se depositan en el contenedor " +
                                                "amarillo, junto con otros envases de plástico, latas y briks, donde se reciclan y se transforman en nuevos productos de plástico " +
                                                "y cartón, contribuyendo a la reducción de residuos y al cuidado del medio ambiente.",
                                        contenedorProducto = "Contenedor amarillo"
                                    )
                                )
                            )
                        }
                    }
                }).build()

            INSTANCE = instance // se guarda la instancia creada

            // Room no ejecuta onCreate hasta que se use la base de datos
            // se fuerza una apertura para que se inserten los datos iniciales en el momento
            CoroutineScope(Dispatchers.IO).launch {
                instance.openHelper.writableDatabase
            }

            instance // se devuelve la instancia
        }
    }
}