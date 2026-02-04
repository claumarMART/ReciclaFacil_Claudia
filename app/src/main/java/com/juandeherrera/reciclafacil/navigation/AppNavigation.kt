package com.juandeherrera.reciclafacil.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.juandeherrera.reciclafacil.localdb.AppDB
import com.juandeherrera.reciclafacil.localdb.Estructura
import com.juandeherrera.reciclafacil.screens.*

@Composable
fun AppNavigation() {
    val controladorNavegacion = rememberNavController()
    val context = LocalContext.current

    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME)
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration() // Evita cierres por cambios en la BD
        .build()

    val sesionIniciada = db.sesionDao().existeSesion()

    NavHost(
        navController = controladorNavegacion,
        startDestination = if (sesionIniciada) AppScreens.inicio.route else AppScreens.login.route
    ) {
        // Se añade "_ ->" para corregir el error de tipo de argumento
        composable(route = AppScreens.login.route) { _ ->
            PantallaLogin(controladorNavegacion)
        }

        composable(route = AppScreens.crearUsuario.route) { _ ->
            BackHandler(enabled = true) {}
            PantallaCrearUsuario(controladorNavegacion)
        }

        composable(route = AppScreens.inicio.route) { _ ->
            BackHandler(enabled = true) {}
            PantallaInicio(controladorNavegacion)
        }

        composable(route = AppScreens.busqueda.route) { _ ->
            BackHandler(enabled = true) {}
            PantallaBusqueda(controladorNavegacion)
        }

        composable(route = AppScreens.historial.route) { _ ->
            BackHandler(enabled = true) {}
            PantallaHistorial(controladorNavegacion)
        }

        composable(route = AppScreens.perfil.route) { _ ->
            BackHandler(enabled = true) {}
            PantallaPerfil(controladorNavegacion)
        }

        composable(
            route = AppScreens.producto.route + "/{idProducto}",
            arguments = listOf(navArgument("idProducto") { type = NavType.IntType })
        ) { backStackEntry ->
            val idProducto = backStackEntry.arguments?.getInt("idProducto")
            if (idProducto != null) {
                PantallaProducto(controladorNavegacion, idProducto)
            }
        }
    }
}