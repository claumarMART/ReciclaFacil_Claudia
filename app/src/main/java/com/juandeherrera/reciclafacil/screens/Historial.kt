package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.juandeherrera.reciclafacil.localdb.AppDB
import com.juandeherrera.reciclafacil.localdb.Estructura
import com.juandeherrera.reciclafacil.localdb.ProductoData
import com.juandeherrera.reciclafacil.navigation.AppScreens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorial(controladorNavegacion: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember {
        Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    var listaReciclados by remember { mutableStateOf<List<ProductoData>>(emptyList()) }
    val usuario = remember { db.sesionDao().obtenerUsuario() }

    LaunchedEffect(Unit) {
        usuario?.let { u ->
            val datos = withContext(Dispatchers.IO) {
                db.historialDao().obtenerHistorialUsuario(u.idUsuario)
            }
            listaReciclados = datos
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mi Historial",
                        style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 24.sp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF34BB00),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                // LISTA DE ICONOS IGUAL QUE EN PERFIL
                val items = listOf(
                    Triple("Inicio", Icons.Default.Home, AppScreens.inicio.route),
                    Triple("Búsqueda", Icons.Default.Search, AppScreens.busqueda.route),
                    Triple("Escáner", Icons.Default.PhotoCamera, ""),
                    Triple("Historial", Icons.Default.History, AppScreens.historial.route),
                    Triple("Perfil", Icons.Default.Person, AppScreens.perfil.route)
                )
                items.forEach { (label, icon, route) ->
                    NavigationBarItem(
                        selected = label == "Historial", // MARCADO COMO SELECCIONADO
                        onClick = {
                            if(route.isNotEmpty() && label != "Historial") {
                                controladorNavegacion.navigate(route)
                            } else if (route.isEmpty()) {
                                Toast.makeText(context, "No disponible", Toast.LENGTH_SHORT).show()
                            }
                        },
                        icon = { Icon(icon, contentDescription = label, modifier = Modifier.size(30.dp)) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Green,
                            indicatorColor = Color(0xFFCEFFD1),
                            unselectedIconColor = Color.Black,
                            unselectedTextColor = Color.Black
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFE0F8D9))
        ) {
            if (listaReciclados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay registros aún", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = listaReciclados) { producto ->

                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = { valor ->
                                if (valor == SwipeToDismissBoxValue.StartToEnd) {
                                    scope.launch(Dispatchers.IO) {
                                        usuario?.let { u ->
                                            db.historialDao().eliminarRegistro(u.idUsuario, producto.idProducto)
                                            withContext(Dispatchers.Main) {
                                                listaReciclados = listaReciclados.filter { it != producto }
                                                Toast.makeText(context, "${producto.tituloProducto} eliminado", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                    true
                                } else false
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromEndToStart = false,
                            backgroundContent = {
                                val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Color.Red else Color.Transparent
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color, shape = CardDefaults.shape)
                                        .padding(start = 20.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Icon(Icons.Default.Delete, "Borrar", tint = Color.White)
                                }
                            }
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Eco, null, tint = Color(0xFF34BB00))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(producto.tituloProducto, fontWeight = FontWeight.Bold)
                                        Text("Contenedor: ${producto.contenedorProducto}", fontSize = 14.sp)
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