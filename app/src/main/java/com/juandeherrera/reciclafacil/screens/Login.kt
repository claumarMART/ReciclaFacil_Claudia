package com.juandeherrera.reciclafacil.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.juandeherrera.reciclafacil.R
import com.juandeherrera.reciclafacil.localdb.AppDB
import com.juandeherrera.reciclafacil.localdb.Estructura
import com.juandeherrera.reciclafacil.localdb.SesionData
import com.juandeherrera.reciclafacil.navigation.AppScreens
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(controladorNavegacion: NavController) {

    // Variables de estado corregidas
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") } //Cambiado a String
    var passVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Imagen del titulo"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bienvenid@ a ",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Reciclafacil",
                    color = Color(0xFF34BB00),
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { if (it.length < 40) email = it },
                label = { Text("Email") },
                modifier = Modifier.width(300.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF34BB00),
                    unfocusedBorderColor = Color(0xFF34BB00),
                    cursorColor = Color(0xFF34BB00),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo Contraseña CORREGIDO
            OutlinedTextField(
                value = password,
                onValueChange = { if (it.length < 20) password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.width(300.dp),
                // Aquí controlamos la visibilidad
                visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passVisible = !passVisible }) {
                        Icon(
                            imageVector = if (passVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passVisible) "Ocultar" else "Mostrar"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF34BB00),
                    unfocusedBorderColor = Color(0xFF34BB00),
                    cursorColor = Color(0xFF34BB00),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedLabelColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    iniciarSesion(controladorNavegacion, email, password, context)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF34BB00),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Iniciar sesión",
                    style = TextStyle(fontSize = 20.sp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿No tienes cuenta? ", color = Color.Black)
                Text(
                    text = "Regístrate",
                    color = Color(0xFF34BB00),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        controladorNavegacion.navigate(AppScreens.crearUsuario.route)
                    }
                )
            }
        }
    }
}

fun iniciarSesion(controladorNavegacion: NavController, email: String, password: String, context: Context) {
    val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME).allowMainThreadQueries().build()
    val usuarioem = db.usuarioDao().getUser(email)

    when {
        email.isBlank() -> Toast.makeText(context, "El email no puede estar vacio", Toast.LENGTH_SHORT).show()
        !email.matches(emailPattern) -> Toast.makeText(context, "Formato de email no valido", Toast.LENGTH_SHORT).show()
        password.length != 8 -> Toast.makeText(context, "La contraseña debe tener 8 caracteres", Toast.LENGTH_SHORT).show()
        usuarioem == null -> Toast.makeText(context, "No existe el usuario", Toast.LENGTH_SHORT).show()
        password != usuarioem.passwordUsuario -> Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
        else -> {
            val sesion = SesionData(
                idUsuarioSesion = usuarioem.idUsuario,
                fechaInicioSesion = obtenerFechaHora()
            )
            db.sesionDao().nuevaSesion(sesion)
            controladorNavegacion.navigate(route = AppScreens.inicio.route)
        }
    }
}

fun obtenerFechaHora(): String {
    val fechaHoraActual = LocalDateTime.now()
    val formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    return fechaHoraActual.format(formato)
}