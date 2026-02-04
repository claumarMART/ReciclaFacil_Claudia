package com.juandeherrera.reciclafacil.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistorialDAO {
    @Query("SELECT p.* FROM ${Estructura.Historial.TABLE_NAME} h " +
            "INNER JOIN ${Estructura.Producto.TABLE_NAME} p ON h.${Estructura.Historial.ID_PRODUCTO} = p.${Estructura.Producto.ID} " +
            "WHERE h.${Estructura.Historial.ID_USUARIO} = :idUsuarioSesion")
    suspend fun obtenerHistorialUsuario(idUsuarioSesion: Int): List<ProductoData>

    @Insert
    suspend fun nuevoRegistro(historialData: HistorialData)

    @Query("DELETE FROM ${Estructura.Historial.TABLE_NAME} WHERE ${Estructura.Historial.ID_USUARIO} = :idUser AND ${Estructura.Historial.ID_PRODUCTO} = :idProd")
    suspend fun eliminarRegistro(idUser: Int, idProd: Int)
}

