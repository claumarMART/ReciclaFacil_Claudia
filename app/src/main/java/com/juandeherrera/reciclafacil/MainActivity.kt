package com.juandeherrera.reciclafacil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.juandeherrera.reciclafacil.localdb.DatabaseProvider
import com.juandeherrera.reciclafacil.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DatabaseProvider.getDatabase(this) // al iniciar se crea la base

        // se permite que la aplicacion cree su contenido debajo de las barras del sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // controlador para mostrar/ocultar las barras del sistema
        val controller = WindowInsetsControllerCompat(window, window.decorView)

        controller.hide(WindowInsetsCompat.Type.systemBars())  // se oculta todas las barras del sistema (superior e inferior)

        // define el comportamiento de las barras ocultas -> el usuario las muestra temporalmente al deslizar el borde de la pantalla
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        enableEdgeToEdge()
        setContent {
            AppNavigation() // se encarga de manegar la navegacion y mostrar la primera pantalla
        }
    }
}
