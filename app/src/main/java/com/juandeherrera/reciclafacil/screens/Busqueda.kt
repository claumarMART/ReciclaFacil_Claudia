package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.juandeherrera.reciclafacil.localdb.AppDB
import com.juandeherrera.reciclafacil.localdb.Estructura
import com.juandeherrera.reciclafacil.localdb.ProductoData
import com.juandeherrera.reciclafacil.metodosAuxiliares.obtenerPainterProducto
import com.juandeherrera.reciclafacil.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaBusqueda(controladorNavegacion: NavController) {

    var textoBusqueda by remember { mutableStateOf("") } // estado para el texto de la barra de busqueda

    val context = LocalContext.current // se obtiene el contexto actual (necesario para la bd local y mostrar mensajes Toasts)

    // instancia a la base de datos local
    // se indica el contexto, nombre del archivo, permitiendo operaciones en el hilo principal
    // con allowMainThreadQueries() se hace que el manejo de la base de datos y la app corran en el mismo hilo (no es lo mas recomendable)
    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME).allowMainThreadQueries().build()

    val listaProductos = db.productoDao().getListaProductos() // lista completa de los productos de la base de datos

    // lista de los productos filtrados segun el texto introducido
    val productosFiltrados = listaProductos.filter {
        it.tituloProducto.contains(textoBusqueda, ignoreCase = true) // buscar sin importar las mayusculas
    }

    Scaffold(
        // BARRA SUPERIOR
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(100.dp),       // altura de la barra superior
                title = {
                    Text(
                        text = "Búsqueda", // texto del titulo de la barra superior
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
                actions = {
                    Text(
                        text = "Filtros",
                        color = Color.White,   // color del texto del titulo
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                            fontSize = 20.sp,                    // tamaño de fuente del titulo
                        ),
                        modifier = Modifier.padding(end = 10.dp).clickable {}
                    )
                }
            )
        },
        // BARRA INFERIOR
        bottomBar = {
            // BARRA DE NAVEGACION
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        controladorNavegacion.navigate(AppScreens.inicio.route) // se navega a la opcion de inicio
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,  // icono
                            contentDescription = "inicio",      // descripcion del icono
                            modifier = Modifier.size(30.dp)    // tamaño del icono
                        )
                    },
                    label = {
                        Text(
                            text = "Inicio",    // texto
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,  // fuente tipografica del texto
                                fontSize = 14.sp,                   // tamaño de fuente del texto
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Green,       // color del icono al ser seleccionado
                        selectedTextColor = Color.Green,       // color del texto al ser seleccionado
                        indicatorColor = Color(0xFFCEFFD1), // color de fondo del item seleccionado
                        unselectedIconColor = Color.Black,     // color del icono no seleccionado
                        unselectedTextColor = Color.Black      // color del texto no seleccionado
                    )
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        controladorNavegacion.navigate(AppScreens.busqueda.route) // se navega a la opcion de busqueda
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Search,  // icono
                            contentDescription = "buscar",      // descripcion del icono
                            modifier = Modifier.size(30.dp)    // tamaño del icono
                        )
                    },
                    label = {
                        Text(
                            text = "Busqueda",    // texto
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,  // fuente tipografica del texto
                                fontSize = 14.sp,                   // tamaño de fuente del texto
                                fontWeight = FontWeight.Bold        // texto con negrita
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Green,       // color del icono al ser seleccionado
                        selectedTextColor = Color.Green,       // color del texto al ser seleccionado
                        indicatorColor = Color(0xFFCEFFD1), // color de fondo del item seleccionado
                        unselectedIconColor = Color.Black,     // color del icono no seleccionado
                        unselectedTextColor = Color.Black      // color del texto no seleccionado
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        Toast.makeText(context, "Función no disponible", Toast.LENGTH_SHORT).show() // notificacion de bloqueo de la funcion de camara
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,  // icono
                            contentDescription = "escaner",      // descripcion del icono
                            modifier = Modifier.size(30.dp)    // tamaño del icono
                        )
                    },
                    label = {
                        Text(
                            text = "Escáner",    // texto
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,  // fuente tipografica del texto
                                fontSize = 14.sp                   // tamaño de fuente del texto
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Green,       // color del icono al ser seleccionado
                        selectedTextColor = Color.Green,       // color del texto al ser seleccionado
                        indicatorColor = Color(0xFFCEFFD1), // color de fondo del item seleccionado
                        unselectedIconColor = Color.Black,     // color del icono no seleccionado
                        unselectedTextColor = Color.Black      // color del texto no seleccionado
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        controladorNavegacion.navigate(AppScreens.historial.route) // se navega a la opcion de historial
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.History,  // icono
                            contentDescription = "historial",      // descripcion del icono
                            modifier = Modifier.size(30.dp)    // tamaño del icono
                        )
                    },
                    label = {
                        Text(
                            text = "Historial",    // texto
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,  // fuente tipografica del texto
                                fontSize = 14.sp                   // tamaño de fuente del texto
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Green,       // color del icono al ser seleccionado
                        selectedTextColor = Color.Green,       // color del texto al ser seleccionado
                        indicatorColor = Color(0xFFCEFFD1), // color de fondo del item seleccionado
                        unselectedIconColor = Color.Black,     // color del icono no seleccionado
                        unselectedTextColor = Color.Black      // color del texto no seleccionado
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        controladorNavegacion.navigate(AppScreens.perfil.route) // se navega a la opcion de perfil
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,  // icono
                            contentDescription = "perfil",      // descripcion del icono
                            modifier = Modifier.size(30.dp)    // tamaño del icono
                        )
                    },
                    label = {
                        Text(
                            text = "Perfil",    // texto
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,  // fuente tipografica del texto
                                fontSize = 14.sp                   // tamaño de fuente del texto
                            )
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Green,       // color del icono al ser seleccionado
                        selectedTextColor = Color.Green,       // color del texto al ser seleccionado
                        indicatorColor = Color(0xFFCEFFD1), // color de fondo del item seleccionado
                        unselectedIconColor = Color.Black,     // color del icono no seleccionado
                        unselectedTextColor = Color.Black      // color del texto no seleccionado
                    )
                )
            }
        }
    ){
        innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize()                 // ocupa el espacio disponible
                .padding(innerPadding) // usa el padding por defecto
                .background(Color(0xFFE0F8D9)),             // color de fondo
            horizontalAlignment = Alignment.CenterHorizontally,   // centrado horizontal
        ){

            // barra de busqueda
            OutlinedTextField(
                value = textoBusqueda, // valor de la barra de busqueda
                onValueChange = { if (it.length < 40){ textoBusqueda = it } },  // se limita la longitud a 40 caracteres
                modifier = Modifier.fillMaxWidth() // abarca el ancho de la pantalla
                    .padding(16.dp),          // padding interno
                placeholder = {
                    Text(
                        text = "Buscar producto para reciclar...", // texto
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, // icono que se pone en el lado izquierdo
                        contentDescription = "Buscar"       // descripcion
                    )
                },
                singleLine = true, // el texto solo ocupa una linea
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(0xFF34BB00),   // borde del campo cuando esta activo
                    unfocusedIndicatorColor = Color(0xFF34BB00), // borde del campo cuando no esta activo
                    focusedContainerColor = Color.White,   // color del fondo del campo cuando esta activo
                    unfocusedContainerColor = Color.White, // color del fondo del campo cuando no esta activo
                    cursorColor = Color(0xFF34BB00) // color del cursor en el campo de texto
                )
            )

            // lista de productos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),  // abarca la pantalla
                contentPadding = PaddingValues(16.dp), // padding interno
                verticalArrangement = Arrangement.spacedBy(12.dp) // distancia entre elementos verticalmente
            ){
                // se recorre la lista de productos filtrados
                items(productosFiltrados) { producto ->

                    // productos que se muestran
                    ItemProducto(
                        producto = producto, // informacion del producto
                        onClick = {
                            controladorNavegacion.navigate(AppScreens.producto.route + "/${producto.idProducto}") // al pulsar en el producto se navega a su pantalla
                        }
                    )
                }

            }
        }
    }
}

// funcion auxiliar que muestra el contenido de una busqueda de un producto
@Composable
fun ItemProducto(producto: ProductoData, onClick: () -> Unit) {
    // tarjeta
    Card(
        modifier = Modifier.fillMaxWidth() // ocupa el ancho de la pantalla
            .height(90.dp)         // altura
            .clickable{ onClick() },       // al pulsarla navega a la pantalla del producto
        colors = CardDefaults.cardColors(
            containerColor = Color.White  // color de fondo de la tarjeta
        ),
        elevation = CardDefaults.cardElevation(4.dp) // sombreado de elevacion de la tarjeta
    ){
        // fila que contendrá la tarjeta
        Row(
            modifier = Modifier.fillMaxSize() // ocupa el espacio de la tarjeta
                .padding(horizontal = 16.dp),  // padding en los laterales
            verticalAlignment = Alignment.CenterVertically // centrado vertical
        ){
            // IMAGEN A LA IZQUIERDA
            Image(
                painter = obtenerPainterProducto(producto.tituloProducto), // imagen guardada en drawable
                contentDescription = producto.tituloProducto, // descripcion de la imagen
                modifier = Modifier
                    .size(60.dp)  // tamaño en ambas dimensiones de la imagen
                    .clip(CircleShape),   // forma circular
                contentScale = ContentScale.Crop // la imagen ocupa el circulo
            )

            // TEXTO
            Text(
                text = producto.tituloProducto,  // texto
                color = Color(0xFF2E7D32),  // color del texto
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,  // fuente tipografica
                    textAlign = TextAlign.Center, // texto alineado en el centro
                    fontSize = 20.sp,  // tamaño de fuente del texto
                    fontWeight = FontWeight.Bold, // texto en negrita
                ),
                modifier = Modifier.weight(1f)  // ocupa el espacio que falta
            )
        }
    }
}