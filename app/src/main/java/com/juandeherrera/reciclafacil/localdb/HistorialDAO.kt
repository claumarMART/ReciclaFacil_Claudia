package com.juandeherrera.reciclafacil.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// OPERACIONES QUE SE REALIZARAN EN LA TABLA USUARIO
@Dao
interface HistorialDAO {
    // OBTENER LOS PRODUCTOS DEL HISTORIAL
    // se usan parametros (:idUsuarioSesion) para evitar inyeccion SQL y que el parametro sea gestionado a través de la funcion
    // se usa ? para comprobar si el resultado es null antes de usarlo
    @Query("SELECT p.* FROM ${Estructura.Historial.TABLE_NAME} h " +
            "INNER JOIN ${Estructura.Producto.TABLE_NAME} p ON h.${Estructura.Historial.ID_PRODUCTO} = p.${Estructura.Producto.ID} " +
            "WHERE h.${Estructura.Historial.ID_USUARIO} = :idUsuarioSesion")
    fun obtenerHistorialUsuario(idUsuarioSesion: Int): List<ProductoData>

    // AGREGAR UN NUEVO USUARIO
    @Insert
    fun nuevoRegistro (historialData: HistorialData)
}