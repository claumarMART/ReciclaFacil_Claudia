package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedSecureTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.X
import com.juandeherrera.reciclafacil.R
import com.juandeherrera.reciclafacil.localdb.AppDB
import com.juandeherrera.reciclafacil.localdb.Estructura
import com.juandeherrera.reciclafacil.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(controladorNavegacion: NavController) {

    val menuAbierto = remember { mutableStateOf(false) } // variable de estado para controlar cuando se abre/cierra el menu del toolbar

    val abirCuadroDialogo = remember { mutableStateOf(false) } // variable para el estado (abierto/cerrado) del cuadro de dialogo

    val formularioAbierto = remember { mutableStateOf(false) } // variable de estado para controlar cuando se abre/cierra el formulario de modificar usuario

    val context = LocalContext.current // se obtiene el contexto actual (necesario para la bd local y mostrar mensajes Toasts)

    // instancia a la base de datos local
    // se indica el contexto, nombre del archivo, permitiendo operaciones en el hilo principal
    // con allowMainThreadQueries() se hace que el manejo de la base de datos y la app corran en el mismo hilo (no es lo mas recomendable)
    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME).allowMainThreadQueries().build()

    val usuario = db.sesionDao().obtenerUsuario()  // se obtiene los datos del usuario que tiene sesion activa

    Scaffold(
        // BARRA SUPERIOR
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(100.dp),       // altura de la barra superior
                title = {
                    Text(
                        text = "Perfil", // texto del titulo de la barra superior
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
                        text = "Ajustes",
                        color = Color.White,   // color del texto del titulo
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                            fontSize = 20.sp,                    // tamaño de fuente del titulo
                        ),
                        modifier = Modifier.padding(start = 10.dp).clickable {
                            menuAbierto.value = true // se abre el menu desplegable
                        }
                    )

                    // menu desplegable
                    DropdownMenu(
                        expanded = menuAbierto.value,  // controla el estado (abrir/cerrar) el menu
                        onDismissRequest = { menuAbierto.value = false }, // se cierra al pulsar afuera del menu
                        modifier = Modifier.background(Color(0xFFCAFFCA).copy(alpha = 0.3f)) // color de fondo del menu desplegable
                    ){
                        // elemento del menu para editar usuario
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Modificar mis datos",   // texto del elemento
                                    color = Color.Black,           // color del texto
                                    style = TextStyle(
                                        fontFamily = FontFamily.SansSerif,  // fuente tipografica
                                        fontSize = 18.sp     // tamaño de fuente
                                    )
                                )
                            },
                            onClick = {
                                menuAbierto.value = false       // se cierra el menu desplegable
                                formularioAbierto.value = true; // se abre el modal con el formulario para editar
                            }
                        )

                        // elemento del menu para editar usuario
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Eliminar mi cuenta",        // texto del elemento
                                    color = Color.Black,     // color del texto de
                                    style = TextStyle(
                                        fontFamily = FontFamily.SansSerif,  // fuente tipografica
                                        fontSize = 18.sp     // tamaño de fuente
                                    )
                                )
                            },
                            onClick = {
                                menuAbierto.value = false       // se cierra el menu desplegable
                                abirCuadroDialogo.value = true; // se abre el cuadro de dialogo para confirmar la eliminacion de cuenta
                            }
                        )
                    }
                },
                actions = {
                    Text(
                        text = "Cerrar sesión",
                        color = Color.White,   // color del texto del titulo
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                            fontSize = 20.sp,                    // tamaño de fuente del titulo
                        ),
                        modifier = Modifier.padding(end = 10.dp).clickable {
                            db.sesionDao().eliminarSesion(usuario.idUsuario) // elimina la sesion del usuario
                            controladorNavegacion.navigate(AppScreens.login.route) // se vuelve a la pantalla de login
                        }
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
                    selected = false,
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
                    selected = true,
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
            }
        }
    ){
        innerPadding ->

        // si el estado del cuadro de dialogo es true, se muestra el cuadro de dialogo
        if (abirCuadroDialogo.value) {
            // cuadro de dialogo
            BasicAlertDialog(
                onDismissRequest = { abirCuadroDialogo.value = false }  // se cierra el cuadro de dialogo cuando se pulsa afuera
            ){
                ElevatedCard(
                    shape = MaterialTheme.shapes.large, // bordes redondeados
                    modifier = Modifier.fillMaxWidth()  // ocupa el espacio disponible
                        .padding(24.dp)            // padding interno
                ){
                    Column(
                        modifier = Modifier.padding(20.dp), // padding interno
                        horizontalAlignment = Alignment.CenterHorizontally  // se centra horizontal
                    ){
                        Text(
                            text = "Eliminar tu cuenta",    // texto del titulo
                            color = Color(0xFF34BB00),   // color del texto del titulo
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                                fontSize = 20.sp,                   // tamaño de fuente del titulo
                                fontWeight = FontWeight.Bold        // texto en negrita
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))  // separacion vertical entre componentes

                        Text(
                            text = "¿Estás segur@ de eliminar tu cuenta? Esta acción no se podrá deshacer.", // texto del cuerpo
                            color = Color.Black,   // color del texto del cuerpo
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,  // fuente tipografica del cuerpo
                                fontSize = 17.sp,                    // tamaño de fuente del cuerpo
                                textAlign = TextAlign.Justify       // texto justificado
                            )
                        )

                        Spacer(modifier = Modifier.height(18.dp))  // separacion vertical entre componentes

                        Row(
                            horizontalArrangement = Arrangement.End,        // alineacion horizontal a la derecha
                            verticalAlignment = Alignment.CenterVertically, // centrado vertical
                            modifier = Modifier.fillMaxWidth()              // ocupa el espacio disponible
                        ){
                            // Texto clickable para cancelar
                            Text(
                                text = "Cancelar",   // texto
                                color = Color(0xFF34BB00),   // color del texto
                                style = TextStyle(
                                    fontFamily = FontFamily.SansSerif,  // fuente tipografica del cuerpo
                                    fontSize = 16.sp                    // tamaño de fuente del cuerpo
                                ),
                                modifier = Modifier.padding(8.dp) // padding
                                    .clickable {
                                        abirCuadroDialogo.value = false // se cierra el cuadro de dialogo
                                    }
                            )

                            Spacer(modifier = Modifier.width(16.dp))  // separacion horizontal entre componentes

                            Button(
                                onClick = {
                                    db.usuarioDao().eliminarUsuario(usuario.idUsuario)     // se elimina el usuario
                                    controladorNavegacion.navigate(AppScreens.login.route) // se vuelve a la pantalla de login
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF34BB00),      // color de fondo del boton
                                    contentColor = Color.White                      // color del texto del boton
                                )
                            ){
                                Text(
                                    text = "Eliminar",    // texto del boton
                                    style = TextStyle(
                                        fontFamily = FontFamily.SansSerif,  // fuente tipografica del texto
                                        fontSize = 16.sp                    // tamaño de fuente del texto
                                    )
                                )
                            }
                        }
                    }

                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()                // ocupa el espacio disponible
                .padding(innerPadding)        // usa el padding por defecto
                .background(Color(0xFFE0F8D9)),       // color de fondo
            horizontalAlignment = Alignment.CenterHorizontally,   // centrado horizontal
            verticalArrangement = Arrangement.Center              // centrado vertical
        ){

            // FOTO DE PERFIL POR DEFECTO
            Image(
                painter = painterResource(id = R.drawable.usuariodefault), // imagen guardada en drawable
                contentDescription = "imagen de perfil", // descripcion de la imagen
                modifier = Modifier
                    .size(180.dp)  // tamaño en ambas dimensiones de la imagen
                    .clip(CircleShape)   // forma circular
            )

            Spacer(modifier = Modifier.height(26.dp)) // separacion vertical entre componentes

            // TARJETA DE INFORMACIÓN DEL USUARIO
            Card(
                modifier = Modifier
                    .padding(horizontal = 24.dp)  // padding de los laterales
                    .fillMaxWidth(),      // ocupa el ancho de la pantalla
                shape = RoundedCornerShape(20.dp),  // bordes redondeados
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // sombra para la elevacion de la tarjeta
                colors = CardDefaults.cardColors(
                    containerColor = Color.White  // color de fondo de la tarjeta
                )
            ){
                Column(
                    modifier = Modifier.fillMaxWidth()         // ocupa el ancho de la pantalla
                        .padding(24.dp),                  // padding interno
                    horizontalAlignment = Alignment.CenterHorizontally,       // centrado horizontal
                    verticalArrangement = Arrangement.spacedBy(16.dp) // espacio vertical entre elementos
                ){

                    // nombre completo del usuario
                    Text(
                        text = usuario.nombreUsuario,  // texto
                        color = Color(0xFF2E7D32),   // color del texto
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica
                            fontSize = 26.sp,                    // tamaño de fuente
                            fontWeight = FontWeight.Bold,       // texto en negrita
                            textAlign = TextAlign.Center        // se centra el texto
                        )
                    )

                    // email del usuario
                    Text(
                        text = usuario.emailUsuario,  // texto
                        color = Color.DarkGray,   // color del texto
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,  // fuente tipografica
                            fontSize = 18.sp,                    // tamaño de fuente
                            textAlign = TextAlign.Center        // se centra el texto
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp)) // separacion vertical entre componentes

            // mensaje ecologico
            Text(
                text = "🌱 Gracias por reciclar y cuidar el planeta 🌱",  // texto
                color = Color(0xFF358639),   // color del texto
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,  // fuente tipografica
                    fontSize = 15.sp                    // tamaño de fuente
                )
            )
        }

        // si el estado del modal es true, se muestra el modal con el formulario para modificar los datos del usuario
        if (formularioAbierto.value) {
            var nombre by remember { mutableStateOf(usuario.nombreUsuario) }
            var email by remember { mutableStateOf(usuario.emailUsuario) }
            val password = rememberTextFieldState()
            var passVisible by remember { mutableStateOf(false) }

            val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}") // patron que debe cumplir el email

            Box(
                modifier = Modifier.fillMaxSize()          // abarca toda la pantalla
                    .padding(innerPadding), // padding con valor predeterminado
                contentAlignment = Alignment.Center        // el contenido esta centrado

            ){
                Column(
                    modifier = Modifier.fillMaxWidth() // abarca el ancho de toda la pantalla
                        .padding(20.dp), // padding interno
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth() // abarca el ancho de toda la pantalla
                            .background(Color.White) // fondo del formulario
                            .border(2.dp, Color.Black)
                            .padding(20.dp)           // padding interno
                    ){
                        // contenedor del modal
                        Column(
                            modifier = Modifier.fillMaxWidth(), // abarca el ancho de toda la pantalla
                            horizontalAlignment = Alignment.CenterHorizontally,   // centrado horizontal
                            verticalArrangement = Arrangement.Center              // centrado vertical
                        ){
                            // fila para el boton de cierre del modal
                            Row(
                                modifier = Modifier.fillMaxWidth(), // abarca el ancho de toda la pantalla
                                verticalAlignment = Alignment.CenterVertically, // centrado vertical
                                horizontalArrangement = Arrangement.End // alineacion horizontal a la derecha
                            ){
                                IconButton(
                                    onClick = {formularioAbierto.value = false}  // cierra el modal
                                ){
                                    Icon(
                                        imageVector = Lucide.X, // icono
                                        contentDescription = "Cerrar modal", // descripcion
                                        modifier = Modifier.size(20.dp),   // tamaño del icono
                                        tint = Color.Black     // color del icono
                                    )
                                }
                            }

                            // fila donde estara la columna con el formulario de modificar el formulario
                            Row(
                                modifier = Modifier.fillMaxWidth(), // abarca el ancho de toda la pantalla
                                verticalAlignment = Alignment.CenterVertically, // centrado vertical
                                horizontalArrangement = Arrangement.Center // centrado horizontal
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,   // centrado horizontal
                                    verticalArrangement = Arrangement.Center              // centrado vertical
                                ){
                                    Spacer(modifier = Modifier.height(10.dp))  // separacion vertical entre componentes

                                    // se pide el nombre del usuario
                                    OutlinedTextField(
                                        value = nombre,  // valor del campo de texto
                                        onValueChange = { if (it.length < 40){ nombre = it } },  // se limita la longitud a 40 caracteres
                                        label = {
                                            Text(
                                                text = "Nombre",  // texto del labal
                                                color = Color.Black          // color del texto de label
                                            )
                                        },
                                        modifier = Modifier.width(300.dp),  // ancho del campo de texto
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color(0xFF34BB00),   // borde del campo cuando esta activo
                                            unfocusedIndicatorColor = Color(0xFF34BB00), // borde del campo cuando no esta activo
                                            focusedContainerColor = Color.White,   // color del fondo del campo cuando esta activo
                                            unfocusedContainerColor = Color.White, // color del fondo del campo cuando no esta activo
                                            cursorColor = Color(0xFF34BB00) // color del cursor en el campo de texto
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))  // separacion vertical entre componentes

                                    // se pide el email del usuario
                                    OutlinedTextField(
                                        value = email,  // valor del campo de texto
                                        onValueChange = { if (it.length < 40){ email = it } },  // se limita la longitud a 40 caracteres
                                        label = {
                                            Text(
                                                text = "Email",  // texto del labal
                                                color = Color.Black          // color del texto de label
                                            )
                                        },
                                        modifier = Modifier.width(300.dp),  // ancho del campo de texto
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color(0xFF34BB00),   // borde del campo cuando esta activo
                                            unfocusedIndicatorColor = Color(0xFF34BB00), // borde del campo cuando no esta activo
                                            focusedContainerColor = Color.White,   // color del fondo del campo cuando esta activo
                                            unfocusedContainerColor = Color.White, // color del fondo del campo cuando no esta activo
                                            cursorColor = Color(0xFF34BB00) // color del cursor en el campo de texto
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))  // separacion vertical entre componentes

                                    // se pide la contraseña del usuario
                                    OutlinedSecureTextField(
                                        state = password,  // estado que contiene el texto introducido (la contraseña)
                                        label = {
                                            Text(
                                                text = "Contraseña",   // texto del labal
                                                color = Color.Black    // color del texto de label
                                            )
                                        },
                                        modifier = Modifier.width(300.dp),  // ancho del campo de texto
                                        trailingIcon = { // icono que va al final del campo de texto
                                            IconButton(
                                                onClick = { passVisible = !passVisible }  // al pulsar el icono cambia el estado para mostrar/ocultar la contraseña
                                            ){
                                                Icon(
                                                    // se cambia el icono si la contraseña es visible o no
                                                    imageVector = if (passVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,

                                                    // se cambia la descripcion para lectores de pantalla en funcion si la contraseña es visible o no
                                                    contentDescription = if (passVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                                )
                                            }
                                        },
                                        // controla como se oculta el texto (lo hace visibible completamente o solo muestra el ultimo caracter)
                                        textObfuscationMode = if (passVisible) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color(0xFF34BB00),   // borde del campo cuando esta activo
                                            unfocusedIndicatorColor = Color(0xFF34BB00), // borde del campo cuando no esta activo
                                            focusedContainerColor = Color.White,   // color del fondo del campo cuando esta activo
                                            unfocusedContainerColor = Color.White, // color del fondo del campo cuando no esta activo
                                            cursorColor = Color(0xFF34BB00) // color del cursor en el campo de texto
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))  // separacion vertical entre componentes

                                    // BOTON DE REGISTRAR USUARIO
                                    Button(
                                        onClick = {
                                            val usuarioem = db.usuarioDao().getUser(email) // se obtiene el usuario (si existiese sino null) a partir del email

                                            // validaciones basicas de los campos de texto
                                            when{
                                                nombre.isBlank() -> {
                                                    Toast.makeText(context, "El nombre no puede estar vacio", Toast.LENGTH_SHORT).show()
                                                }
                                                email.isBlank() -> {
                                                    Toast.makeText(context, "El email no puede estar vacio", Toast.LENGTH_SHORT).show()
                                                }
                                                !email.matches(emailPattern) -> {
                                                    Toast.makeText(context, "El email no tiene un formato valido", Toast.LENGTH_SHORT).show()
                                                }
                                                password.text.length != 8 -> {
                                                    Toast.makeText(context, "La contraseña debe tener 8 caracteres", Toast.LENGTH_SHORT).show()
                                                }
                                                usuarioem == null -> {
                                                    Toast.makeText(context, "Usuario no registrado con ese email", Toast.LENGTH_SHORT).show()
                                                }
                                                else -> {
                                                    // se crea una copia del usuario local con los nuevos datos para mantener los no modificados
                                                    val userActualizado = usuario.copy(
                                                        nombreUsuario = nombre,
                                                        emailUsuario = email,
                                                        passwordUsuario = password.text.toString()

                                                    )

                                                    db.usuarioDao().actualizarUsuario(userActualizado) // se actualiza el usuario en la base de datos local

                                                    Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show() // se muestra un mensaje de confirmacion al usuario

                                                    controladorNavegacion.navigate(route = AppScreens.perfil.route) // se recarga la pantalla de perfil
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF34BB00),  // color de fondo del boton
                                            contentColor = Color.Black                  // color del texto del boton
                                        )
                                    ){
                                        Text(
                                            text = "Modificar datos",     // texto
                                            color = Color.White,         // color del texto del boton
                                            style = TextStyle(
                                                fontSize = 20.sp     // tamaño de la fuente del texto del botón
                                            )
                                        )
                                    }



                                }

                            }



                        }





                    }


                }

            }


        }
    }
}