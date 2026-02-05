package com.juandeherrera.reciclafacil.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.juandeherrera.reciclafacil.localdb.HistorialData
import com.juandeherrera.reciclafacil.metodosAuxiliares.obtenerPainterProducto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaProducto(controladorNavegacion: NavController, idProducto: Int) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Evitamos que la base de datos se recree en cada recomposición
    val db = remember {
        Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    //Obtenemos el usuario y el producto (con remember para estabilidad)
    val usuario = remember { db.sesionDao().obtenerUsuario() }
    val producto = remember { db.productoDao().getProducto(idProducto) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(100.dp),
                title = {
                    Text(
                        text = "Producto",
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 28.sp,
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF34BB00),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    Text(
                        text = "Volver",
                        color = Color.White,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 20.sp,
                        ),
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                controladorNavegacion.popBackStack()
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
                .verticalScroll(state = rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // IMAGEN DEL PRODUCTO
            Image(
                painter = obtenerPainterProducto(producto?.tituloProducto ?: ""),
                contentDescription = producto?.tituloProducto ?: "sin descripcion",
                modifier = Modifier
                    .size(220.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(26.dp))

            // NOMBRE DEL PRODUCTO
            Text(
                text = producto?.tituloProducto ?: "SIN NOMBRE",
                color = Color(0xFF2E7D32),
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // DESCRIPCIÓN
            Text(
                text = producto?.descripcionProducto ?: "SIN DESCRIPCION",
                color = Color.DarkGray,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Justify
                ),
                modifier = Modifier.padding(horizontal = 22.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN DE RECICLAR
            Button(
                onClick = {
                    // Lanzamos la corrutina en el hilo de IO
                    scope.launch(Dispatchers.IO) {
                        try {
                            if (usuario != null) {
                                val registroReciclaje = HistorialData(
                                    idUsuarioSesion = usuario.idUsuario,
                                    idProductoVisitado = idProducto
                                )

                                // Operación de base de datos (suspend)
                                db.historialDao().nuevoRegistro(registroReciclaje)

                                // Volvemos al hilo principal para el Toast y la navegación
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Producto reciclado :)", Toast.LENGTH_SHORT).show()
                                    controladorNavegacion.popBackStack()
                                }
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(context, "Error al guardar registro", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF34BB00),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Reciclar en ${producto?.contenedorProducto?.lowercase() ?: ""}",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 20.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}