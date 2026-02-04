package com.juandeherrera.reciclafacil.screens

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
import com.juandeherrera.reciclafacil.localdb.UsuarioData
import com.juandeherrera.reciclafacil.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearUsuario(controladorNavegacion: NavController) {

    // Estados para los campos de texto
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passVisible by remember { mutableStateOf(false) }

    val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
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
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Reciclafacil",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Títulos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Regístrate en ",
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Reciclafacil",
                    color = Color(0xFF34BB00),
                    style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Campo Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { if (it.length < 40) nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.width(300.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF34BB00),
                    unfocusedBorderColor = Color(0xFF34BB00),
                    cursorColor = Color(0xFF34BB00),
                    focusedLabelColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

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
                    focusedLabelColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Campo Contraseña (Corregido con OutlinedTextFieldDefaults)
            OutlinedTextField(
                value = password,
                onValueChange = { if (it.length <= 8) password = it },
                label = { Text("Contraseña (8 caracteres)") },
                modifier = Modifier.width(300.dp),
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
                    focusedLabelColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Botón Registro
            Button(
                onClick = {
                    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME)
                        .allowMainThreadQueries()
                        .build()

                    val usuarioExistente = db.usuarioDao().getUser(email)

                    when {
                        nombre.isBlank() -> Toast.makeText(context, "El nombre está vacío", Toast.LENGTH_SHORT).show()
                        email.isBlank() -> Toast.makeText(context, "El email está vacío", Toast.LENGTH_SHORT).show()
                        !email.matches(emailPattern) -> Toast.makeText(context, "Formato de email inválido", Toast.LENGTH_SHORT).show()
                        password.length != 8 -> Toast.makeText(context, "La contraseña requiere 8 caracteres", Toast.LENGTH_SHORT).show()
                        usuarioExistente != null -> Toast.makeText(context, "Este email ya está registrado", Toast.LENGTH_SHORT).show()
                        else -> {
                            val user = UsuarioData(
                                nombreUsuario = nombre,
                                emailUsuario = email,
                                passwordUsuario = password
                            )
                            db.usuarioDao().nuevoUsuario(user)
                            Toast.makeText(context, "¡Usuario creado con éxito!", Toast.LENGTH_LONG).show()
                            controladorNavegacion.navigate(AppScreens.login.route)
                        }
                    }
                },
                modifier = Modifier.width(300.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34BB00))
            ) {
                Text("Regístrate", fontSize = 20.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navegación a Login
            Row(
                modifier = Modifier.clickable { controladorNavegacion.navigate(AppScreens.login.route) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "¿Ya tienes una cuenta? ", color = Color.Black)
                Text(
                    text = "Inicia sesión",
                    color = Color(0xFF34BB00),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}