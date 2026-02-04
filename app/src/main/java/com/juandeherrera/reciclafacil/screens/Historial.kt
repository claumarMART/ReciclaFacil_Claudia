package com.juandeherrera.reciclafacil.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
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
                modifier = Modifier.height(100.dp),
                title = {
                    Text(
                        "Mi Historial",
                        style = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 28.sp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF34BB00),
                    titleContentColor = Color.White
                ),
                // BOTÓN DE INICIO A LA IZQUIERDA
                navigationIcon = {
                    Text(
                        text = "Volver",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 20.sp,

                        ),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                // Navega a la ruta "inicio" y limpia la pila
                                controladorNavegacion.navigate(AppScreens.inicio.route) {
                                    popUpTo(AppScreens.inicio.route) { inclusive = true }
                                }
                            }
                    )
                }
            )
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