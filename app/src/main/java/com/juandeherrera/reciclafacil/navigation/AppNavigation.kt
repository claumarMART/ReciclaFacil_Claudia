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
import com.juandeherrera.reciclafacil.screens.PantallaBusqueda
import com.juandeherrera.reciclafacil.screens.PantallaCrearUsuario
import com.juandeherrera.reciclafacil.screens.PantallaHistorial
import com.juandeherrera.reciclafacil.screens.PantallaInicio
import com.juandeherrera.reciclafacil.screens.PantallaLogin
import com.juandeherrera.reciclafacil.screens.PantallaPerfil
import com.juandeherrera.reciclafacil.screens.PantallaProducto

@Composable
fun AppNavigation() {
    val controladorNavegacion = rememberNavController()  // controlador del estado navegación entre las pantallas para desplazarse entre ellas

    val context = LocalContext.current // se obtiene el contexto actual (necesario para la bd local)

    // instancia a la base de datos local
    // se indica el contexto, nombre del archivo, permitiendo operaciones en el hilo principal
    // con allowMainThreadQueries() se hace que el manejo de la base de datos y la app corran en el mismo hilo (no es lo mas recomendable)
    val db = Room.databaseBuilder(context, AppDB::class.java, Estructura.DB.NAME).allowMainThreadQueries().build()

    val sesionIniciada = db.sesionDao().existeSesion() // comprobamos si hay alguna sesion iniciada previamente

    // contenedor que gestiona la navegacion y muestra las pantallas segun la ruta actual
    // se le pasa el controlador del estado de navegacion y la pantalla inicial al abrir la app
    // se muestre el login o la pantalla de perfil en funcion si exista una sesion iniciada
    NavHost(navController = controladorNavegacion, startDestination = if (sesionIniciada) AppScreens.inicio.route else AppScreens.login.route) {

        // se define la ruta para la pantalla y se le indica al navegador la función que ejecutará
        composable(route = AppScreens.login.route) { PantallaLogin(controladorNavegacion) }

        composable(route = AppScreens.crearUsuario.route) {
            BackHandler(true) {} // intercepta que el usuario pueda ir atras con el boton fisico atrás del dispositivo
            PantallaCrearUsuario(controladorNavegacion)
        }

        composable(route = AppScreens.inicio.route) {
            BackHandler(true) {} // intercepta que el usuario pueda ir atras con el boton fisico atrás del dispositivo
            PantallaInicio(controladorNavegacion)
        }

        composable(route = AppScreens.busqueda.route) {
            BackHandler(true) {} // intercepta que el usuario pueda ir atras con el boton fisico atrás del dispositivo
            PantallaBusqueda(controladorNavegacion)
        }

        composable(route = AppScreens.historial.route) {
            BackHandler(true) {} // intercepta que el usuario pueda ir atras con el boton fisico atrás del dispositivo
            PantallaHistorial(controladorNavegacion)
        }

        composable(route = AppScreens.perfil.route) {
            BackHandler(true) {} // intercepta que el usuario pueda ir atras con el boton fisico atrás del dispositivo
            PantallaPerfil(controladorNavegacion)
        }

        composable(
            route = AppScreens.producto.route + "/{idProducto}", // ruta
            arguments = listOf(
                navArgument("idProducto"){ type = NavType.IntType } // el tipo de argumento
            )
        ){ backStackEntry ->
            BackHandler(true) {} // intercepta que el usuario pueda ir atras con el boton fisico atrás del dispositivo

            val idProducto = backStackEntry.arguments?.getInt("idProducto") // se obtiene el valor del argumento

            // si el argumento no es nulo, se hace la navegacion
            if (idProducto != null) {
                PantallaProducto(controladorNavegacion, idProducto)
            }
        }
    }
}