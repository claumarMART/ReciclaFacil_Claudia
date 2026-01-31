package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.juandeherrera.reciclafacil.R
import com.juandeherrera.reciclafacil.localdb.AppDB
import com.juandeherrera.reciclafacil.localdb.Estructura
import com.juandeherrera.reciclafacil.localdb.HistorialData
import com.juandeherrera.reciclafacil.metodosAuxiliares.obtenerPainterProducto
import com.juandeherrera.reciclafacil.navigation.AppScreens
import java.util.Locale
import java.util.Locale.getDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProducto(controladorNavegacion: NavController, idProducto: Int) {


    val context = LocalContext.current // se obtiene el contexto actual (necesario para la bd local y mostrar mensajes Toasts)

    // instancia a la base de datos local
    // se indica el contexto, nombre del archivo, permitiendo operaciones en el hilo principal
    // con allowMainThreadQueries() se hace que el manejo de la base de datos y la app corran en el mismo hilo (no es lo mas recomendable)
    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME).allowMainThreadQueries().build()

    val usuario = db.sesionDao().obtenerUsuario()  // se obtiene los datos del usuario que tiene sesion activa

    val producto = db.productoDao().getProducto(idProducto) // producto de la base de datos

    Scaffold(
        // BARRA SUPERIOR
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(100.dp),       // altura de la barra superior
                title = {
                    Text(
                        text = "Producto", // texto del titulo de la barra superior
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                            fontSize = 28.sp,                    // tamaño de fuente del titulo
                        )
                    )
                },
                colors = topAppBarColors(
                    containerColor = Color(0xFF34BB00),   // color de fondo de la barra superior
                    titleContentColor = Color.White              // color del texto del titulo de la barra superior
                ),
                navigationIcon = {
                    Text(
                        text = "Volver",
                        color = Color.White,   // color del texto del titulo
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                            fontSize = 20.sp,                    // tamaño de fuente del titulo
                        ),
                        modifier = Modifier.padding(start = 10.dp).clickable {
                            controladorNavegacion.popBackStack()  // navegas a la pantalla anterior
                        }
                    )
                }
            )
        }
    ){
        innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize()                // ocupa el espacio disponible
                .padding(innerPadding)        // usa el padding por defecto
                .background(Color(0xFFE0F8D9)),       // color de fondo
            horizontalAlignment = Alignment.CenterHorizontally,   // centrado horizontal
            verticalArrangement = Arrangement.Center              // centrado vertical
        ){
            // IMAGEN DEL PRODUCTO
            Image(
                painter = obtenerPainterProducto(producto?.tituloProducto ?: ""), // imagen guardada en drawable
                contentDescription = producto?.tituloProducto ?: "sin descripcion", // descripcion de la imagen
                modifier = Modifier
                    .size(220.dp)  // tamaño en ambas dimensiones de la imagen
                    .clip(CircleShape),   // forma circular
                contentScale = ContentScale.Crop // la imagen ocupa el circulo
            )

            Spacer(modifier = Modifier.height(26.dp)) // separacion vertical entre componentes

            // NOMBRE DEL PRODUCTO
            Text(
                text = producto?.tituloProducto ?: "SIN NOMBRE",  // texto
                color = Color(0xFF2E7D32),   // color del texto
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,  // fuente tipografica
                    fontSize = 28.sp,                    // tamaño de fuente
                    fontWeight = FontWeight.Bold,       // texto en negrita
                    textAlign = TextAlign.Center        // se centra el texto
                )
            )

            Spacer(modifier = Modifier.height(24.dp)) // separacion vertical entre componentes

            // NOMBRE DEL PRODUCTO
            Text(
                text = producto?.descripcionProducto ?: "SIN DESCRIPCION",  // texto
                color = Color.DarkGray,   // color del texto
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,  // fuente tipografica
                    fontSize = 18.sp,                    // tamaño de fuente
                    textAlign = TextAlign.Justify        // se justifica el texto
                ),
                modifier = Modifier.padding(horizontal = 22.dp) // padding en los laterales
            )

            Spacer(modifier = Modifier.height(24.dp)) // separacion vertical entre componentes

            Button(
                onClick = {
                    // se almacena el registro en el historial
                    val registroReciclaje = HistorialData(
                        idUsuarioSesion = usuario.idUsuario,
                        idProductoVisitado = idProducto
                    )

                    db.historialDao().nuevoRegistro(registroReciclaje) // se agrega el registro de reciclaje

                    Toast.makeText(context, "Producto reciclado :)", Toast.LENGTH_SHORT).show() // se muestra un mensaje de confirmacion al usuario

                    controladorNavegacion.popBackStack()  // navegas a la pantalla anterior


                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF34BB00),  // color de fondo del boton
                    contentColor = Color.White                  // color del texto del boton
                )
            ){
                Text(
                    text = "Reciclar en " + producto?.contenedorProducto?.lowercase(),  // texto
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,  // fuente tipografica
                        fontSize = 20.sp     // tamaño de la fuente del texto del botón
                    )
                )
            }
        }
    }
}