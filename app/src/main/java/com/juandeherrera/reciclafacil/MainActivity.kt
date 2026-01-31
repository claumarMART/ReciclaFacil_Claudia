package com.juandeherrera.reciclafacil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.juandeherrera.reciclafacil.localdb.DatabaseProvider
import com.juandeherrera.reciclafacil.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DatabaseProvider.getDatabase(this) // al iniciar se crea la base
        enableEdgeToEdge()
        setContent {
            AppNavigation() // se encarga de manegar la navegacion y mostrar la primera pantalla
        }
    }
}
