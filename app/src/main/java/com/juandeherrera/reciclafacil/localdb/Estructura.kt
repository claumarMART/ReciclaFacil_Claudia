package com.juandeherrera.reciclafacil.localdb

class Estructura {
    // BASE DE DATOS
    object DB {
        const val NAME = "reciclafacil.db"  // nombre de la bd (crea el archivo con ese nombre)
    }

    // Tabla 1: Usuario
    object Usuario {
        const val TABLE_NAME = "usuario"  // nombre de la tabla

        // nombre de los campos de la tabla
        const val ID = "idUsuario"
        const val NOMBRE = "nombreUsuario"
        const val EMAIL = "emailUsuario"
        const val PASSWORD = "passwordUsuario"
    }

    // Tabla 2: Sesion
    object Sesion {
        const val TABLE_NAME = "sesion" // nombre de la tabla

        // nombre de los campos de la tabla
        const val ID = "idSesion"
        const val ID_USUARIO = "idUsuarioSesion"
        const val FECHA_INICIO_SESION = "fechaInicioSesion"
    }

    // Tabla 3: Producto
    object Producto {
        const val TABLE_NAME = "producto" // nombre de la tabla

        // nombre de los campos de la tabla
        const val ID = "idProducto"
        const val TITULO = "tituloProducto"
        const val DESCRIPCION = "descripcionProducto"
        const val CONTENEDOR = "contenedorProducto"
    }

    // Tabla 4: Historial
    object Historial {
        const val TABLE_NAME = "historial" // nombre de la tabla

        // nombre de los campos de la tabla
        const val ID = "idHistorial"
        const val ID_USUARIO = "idUsuarioSesion"
        const val ID_PRODUCTO = "idProductoVisitado"
    }
}