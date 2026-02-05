package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
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
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaHistorial(controladorNavegacion: NavController) {
    val context = LocalContext.current

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
                ),

            )
        },
        // --- AQUÍ AÑADIMOS LA BARRA INFERIOR IGUAL A LA DE INICIO ---
        bottomBar = {
            NavigationBar {
                // ITEM INICIO
                NavigationBarItem(
                    selected = false,
                    onClick = { controladorNavegacion.navigate(AppScreens.inicio.route) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "inicio", modifier = Modifier.size(30.dp)) },
                    label = { Text("Inicio", style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp)) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Black, unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Green, indicatorColor = Color(0xFFCEFFD1)
                    )
                )
                // ITEM BÚSQUEDA
                NavigationBarItem(
                    selected = false,
                    onClick = { controladorNavegacion.navigate(AppScreens.busqueda.route) },
                    icon = { Icon(Icons.Default.Search, contentDescription = "buscar", modifier = Modifier.size(30.dp)) },
                    label = { Text("Búsqueda", style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp)) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Black, unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Green, indicatorColor = Color(0xFFCEFFD1)
                    )
                )
                // ITEM ESCÁNER
                NavigationBarItem(
                    selected = false,
                    onClick = { Toast.makeText(context, "Función no disponible", Toast.LENGTH_SHORT).show() },
                    icon = { Icon(Icons.Default.PhotoCamera, contentDescription = "escaner", modifier = Modifier.size(30.dp)) },
                    label = { Text("Escáner", style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp)) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Black, unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Green, indicatorColor = Color(0xFFCEFFD1)
                    )
                )
                // ITEM HISTORIAL (Seleccionado)
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Ya estamos aquí */ },
                    icon = { Icon(Icons.Default.History, contentDescription = "historial", modifier = Modifier.size(30.dp)) },
                    label = { Text("Historial", style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, fontWeight = FontWeight.Bold)) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Green, selectedTextColor = Color.Green,
                        indicatorColor = Color(0xFFCEFFD1), unselectedIconColor = Color.Black, unselectedTextColor = Color.Black
                    )
                )
                // ITEM PERFIL
                NavigationBarItem(
                    selected = false,
                    onClick = { controladorNavegacion.navigate(AppScreens.perfil.route) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "perfil", modifier = Modifier.size(30.dp)) },
                    label = { Text("Perfil", style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp)) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Black, unselectedTextColor = Color.Black,
                        selectedIconColor = Color.Green, indicatorColor = Color(0xFFCEFFD1)
                    )
                )
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
                    items(listaReciclados) { producto ->
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