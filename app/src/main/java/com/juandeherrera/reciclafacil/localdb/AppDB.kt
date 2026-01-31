package com.juandeherrera.reciclafacil.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

// la clase define una bd local sqlite que hereda de RoomDatabase (clase para la base de datos Room)
@Database(
    // se establecen las tablas que conforman la bd
    // se indica la version, necesaria para migraciones y cambios realizados
    // se indica que la estructura de la bd se exportará a un archivo para mantener un historial de esquemas
    entities = [UsuarioData::class, SesionData::class, ProductoData::class, HistorialData::class], version = 1, exportSchema = true
)
abstract class AppDB: RoomDatabase() {
    // metodos abstractos que proporcionan instancias de acceso a las operaciones definidas en los DAO
    // se implementara estos metodos para proporcionar la instancia funcional de acceso a los datos
    abstract fun usuarioDao(): UsuarioDAO

    abstract fun sesionDao(): SesionDAO

    abstract fun productoDao(): ProductoDAO

    abstract fun historialDao(): HistorialDAO
}

/*

se puede conectar AppDB con a parte de la forma normal en clase

    val db = DatabaseProvider.getDatabase(context)

*/