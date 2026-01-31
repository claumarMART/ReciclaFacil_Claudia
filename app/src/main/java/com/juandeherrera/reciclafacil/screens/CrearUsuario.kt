package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.juandeherrera.reciclafacil.R
import com.juandeherrera.reciclafacil.localdb.AppDB
import com.juandeherrera.reciclafacil.localdb.DatabaseProvider
import com.juandeherrera.reciclafacil.localdb.Estructura
import com.juandeherrera.reciclafacil.localdb.UsuarioData
import com.juandeherrera.reciclafacil.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearUsuario(controladorNavegacion: NavController) {

    // variables para los datos del formulario
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val password = rememberTextFieldState()
    var passVisible by remember { mutableStateOf(false) }

    val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}") // patron que debe cumplir el email

    val context = LocalContext.current // se obtiene el contexto actual (necesario para la bd local y mostrar mensajes Toasts)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()                 // ocupa el espacio disponible
                .padding(innerPadding) // usa el padding por defecto
                .background(Color.White),             // color de fondo
            horizontalAlignment = Alignment.CenterHorizontally,   // centrado horizontal
            verticalArrangement = Arrangement.Center              // centrado vertical
        ){

            // imagen con el logo de la aplicacion
            Image(
                painter = painterResource(id = R.drawable.logo),  // ruta a la imagen en local
                contentDescription = "Imagen del titulo"          // descripcion
            )

            Spacer(modifier = Modifier.height(20.dp))  // separacion vertical entre componentes

            Row(
                modifier = Modifier.fillMaxWidth(), // ocupa el ancho maximo posible
                horizontalArrangement = Arrangement.Center,  // espaciado horizontal entre elementos
                verticalAlignment = Alignment.CenterVertically     // centrado vertical
            ){
                Text(
                    text = "Regístrate en ",
                    color = Color.Black,   // color del texto del titulo
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                        fontSize = 28.sp,                    // tamaño de fuente del titulo
                        fontWeight = FontWeight.Bold    // texto con negrita
                    )
                )
                Text(
                    text = "Reciclafacil",
                    color = Color(0xFF34BB00),   // color del texto del titulo
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                        fontSize = 28.sp,                    // tamaño de fuente del titulo
                        fontWeight = FontWeight.Bold        // texto con negrita
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))  // separacion vertical entre componentes

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
                    // instancia a la base de datos local
                    // se indica el contexto, nombre del archivo, permitiendo operaciones en el hilo principal
                    // con allowMainThreadQueries() se hace que el manejo de la base de datos y la app corran en el mismo hilo (no es lo mas recomendable)
                    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME).allowMainThreadQueries().build()

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
                        usuarioem != null -> {
                            Toast.makeText(context, "Ya existe un usuario con ese email", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            // se almacena los datos en un usuario para la base de datos local
                            val user = UsuarioData(
                                nombreUsuario = nombre,
                                emailUsuario = email,
                                passwordUsuario = password.text.toString()
                            )

                            db.usuarioDao().nuevoUsuario(user) // se agrega el usuario a la base de datos local

                            Toast.makeText(context, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show() // se muestra un mensaje de confirmacion al usuario

                            controladorNavegacion.navigate(route = AppScreens.login.route) // se regresa a la pantalla de login
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF34BB00),  // color de fondo del boton
                    contentColor = Color.White                  // color del texto del boton
                )
            ){
                Text(
                    text = "Registrate",     // texto
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,  // fuente tipografica
                        fontSize = 20.sp     // tamaño de la fuente del texto del botón
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))  // separacion vertical entre componentes

            Row(
                modifier = Modifier.fillMaxWidth(), // ocupa el ancho maximo posible
                horizontalArrangement = Arrangement.Center,  // espaciado horizontal entre elementos
                verticalAlignment = Alignment.CenterVertically     // centrado vertical
            ){
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = Color.Black,   // color del texto del titulo
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                        fontSize = 18.sp,                    // tamaño de fuente del titulo
                    )
                )
                Text(
                    text = "Inicia sesión",
                    color = Color.Green,   // color del texto del titulo
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,  // fuente tipografica del titulo
                        fontSize = 18.sp,                    // tamaño de fuente del titulo
                        fontWeight = FontWeight.Bold        // texto con negrita
                    ),
                    modifier = Modifier.clickable {
                        controladorNavegacion.navigate(AppScreens.login.route) // vas al formulario de crear el usuario
                    }
                )
            }
        }
    }
}