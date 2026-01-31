package com.juandeherrera.reciclafacil.navigation

// clase sellada en la que a cada pantalla se le asigna la ruta para navegar a ella
sealed class AppScreens (val route: String) {

    object login: AppScreens("login")

    object crearUsuario: AppScreens("crearUsuario")

    object inicio: AppScreens("inicio")

    object busqueda: AppScreens("busqueda")

    object historial: AppScreens("historial")

    object perfil: AppScreens("perfil")

    object producto: AppScreens("producto")
}