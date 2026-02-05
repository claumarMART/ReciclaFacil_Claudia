package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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

    val menuAbierto = remember { mutableStateOf(false) }
    val abirCuadroDialogo = remember { mutableStateOf(false) }
    val formularioAbierto = remember { mutableStateOf(false) }
    val context = LocalContext.current

    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME).allowMainThreadQueries().build()
    val usuario = db.sesionDao().obtenerUsuario()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(100.dp),
                title = {
                    Text(
                        text = "Perfil",
                        style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 28.sp)
                    )
                },
                colors = topAppBarColors(
                    containerColor = Color(0xFF34BB00),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    Text(
                        text = "Ajustes",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 10.dp).clickable { menuAbierto.value = true }
                    )

                    DropdownMenu(
                        expanded = menuAbierto.value,
                        onDismissRequest = { menuAbierto.value = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Modificar mis datos", fontSize = 18.sp) },
                            onClick = {
                                menuAbierto.value = false
                                formularioAbierto.value = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Eliminar mi cuenta", fontSize = 18.sp) },
                            onClick = {
                                menuAbierto.value = false
                                abirCuadroDialogo.value = true
                            }
                        )
                    }
                },
                actions = {
                    Text(
                        text = "Cerrar sesión",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 10.dp).clickable {
                            db.sesionDao().eliminarSesion(usuario.idUsuario)
                            controladorNavegacion.navigate(AppScreens.login.route)
                        }
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    Triple("Inicio", Icons.Default.Home, AppScreens.inicio.route),
                    Triple("Búsqueda", Icons.Default.Search, AppScreens.busqueda.route),
                    Triple("Escáner", Icons.Default.PhotoCamera, ""),
                    Triple("Historial", Icons.Default.History, AppScreens.historial.route),
                    Triple("Perfil", Icons.Default.Person, AppScreens.perfil.route)
                )
                items.forEach { (label, icon, route) ->
                    NavigationBarItem(
                        selected = label == "Perfil",
                        onClick = { if(route.isNotEmpty()) controladorNavegacion.navigate(route) else Toast.makeText(context, "No disponible", Toast.LENGTH_SHORT).show() },
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(30.dp)) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Green,
                            indicatorColor = Color(0xFFCEFFD1)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->

        //CUADRO DE DIÁLOGO ELIMINAR CUENTA
        if (abirCuadroDialogo.value) {
            BasicAlertDialog(onDismissRequest = { abirCuadroDialogo.value = false }) {
                ElevatedCard(modifier = Modifier.padding(24.dp)) {
                    Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Eliminar tu cuenta", color = Color(0xFF34BB00), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("¿Estás segur@? Esta acción es irreversible.", textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            Text("Cancelar", color = Color(0xFF34BB00), modifier = Modifier.padding(8.dp).clickable { abirCuadroDialogo.value = false })
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = {
                                    db.usuarioDao().eliminarUsuario(usuario.idUsuario)
                                    controladorNavegacion.navigate(AppScreens.login.route)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34BB00))
                            ) { Text("Eliminar") }
                        }
                    }
                }
            }
        }

        // CONTENIDO PRINCIPAL
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(Color(0xFFE0F8D9)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.usuariodefault),
                contentDescription = "perfil",
                modifier = Modifier.size(180.dp).clip(CircleShape).border(2.dp, Color.White, CircleShape)
            )
            Spacer(modifier = Modifier.height(26.dp))
            Card(
                modifier = Modifier.padding(horizontal = 24.dp).fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(usuario.nombreUsuario, color = Color(0xFF2E7D32), fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Text(usuario.emailUsuario, color = Color.DarkGray, fontSize = 18.sp)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text("🌱 Gracias por reciclar 🌱", color = Color(0xFF358639), fontSize = 15.sp)
        }

        // MODAL MODIFICAR DATOS
        if (formularioAbierto.value) {
            var nombreMod by remember { mutableStateOf(usuario.nombreUsuario) }
            var emailMod by remember { mutableStateOf(usuario.emailUsuario) }
            var passwordMod by remember { mutableStateOf("") }
            var passVisible by remember { mutableStateOf(false) }
            val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")

            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)).padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f).background(Color.White, RoundedCornerShape(16.dp)).border(2.dp, Color.Black, RoundedCornerShape(16.dp)).padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        IconButton(onClick = { formularioAbierto.value = false }) {
                            Icon(Lucide.X, contentDescription = "Cerrar", modifier = Modifier.size(24.dp))
                        }
                    }
                    Text("Editar Perfil", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF34BB00))
                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedTextField(
                        value = nombreMod,
                        onValueChange = { if (it.length < 40) nombreMod = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF34BB00), unfocusedBorderColor = Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = emailMod,
                        onValueChange = { if (it.length < 40) emailMod = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF34BB00), unfocusedBorderColor = Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = passwordMod,
                        onValueChange = { if (it.length <= 8) passwordMod = it },
                        label = { Text("Nueva Contraseña (8 caracteres)") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            IconButton(onClick = { passVisible = !passVisible }) {
                                Icon(if (passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF34BB00), unfocusedBorderColor = Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            if (nombreMod.isNotBlank() && emailMod.matches(emailPattern) && passwordMod.length == 8) {
                                val userActualizado = usuario.copy(nombreUsuario = nombreMod, emailUsuario = emailMod, passwordUsuario = passwordMod)
                                db.usuarioDao().actualizarUsuario(userActualizado)
                                Toast.makeText(context, "¡Actualizado!", Toast.LENGTH_SHORT).show()
                                formularioAbierto.value = false
                                controladorNavegacion.navigate(AppScreens.perfil.route)
                            } else {
                                Toast.makeText(context, "Datos inválidos", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34BB00))
                    ) { Text("Guardar Cambios", color = Color.White, fontSize = 18.sp) }
                }
            }
        }
    }
}