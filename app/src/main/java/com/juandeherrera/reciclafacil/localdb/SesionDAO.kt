package com.juandeherrera.reciclafacil.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// OPERACIONES QUE SE REALIZARAN EN LA TABLA SESION
@Dao
interface SesionDAO {
    // COMPROBAR QUE EXISTA UNA SESION ALMACENADA
    @Query("SELECT EXISTS(SELECT 1 FROM ${Estructura.Sesion.TABLE_NAME} LIMIT 1)")
    fun existeSesion(): Boolean

    // OBTENER LOS DATOS DEL USUARIO QUE INICIO SESION
    @Query("SELECT u.* FROM ${Estructura.Usuario.TABLE_NAME} u " +
            "INNER JOIN ${Estructura.Sesion.TABLE_NAME} s ON u.${Estructura.Usuario.ID} = s.${Estructura.Sesion.ID_USUARIO} " +
            "LIMIT 1")
    fun obtenerUsuario(): UsuarioData

    // AGREGAR UN NUEVA SESION
    @Insert
    fun nuevaSesion (sesionData: SesionData)

    // ELIMINAR LA SESION A PARTIR DEL ID DEL USUARIO
    // se usan parametros (:idUsuario) para evitar inyeccion SQL y que el parametro sea gestionado a través de la funcion
    // si no habia sesion devuelve 0, pero si la elimino correctamente devolverá 1
    @Query("DELETE FROM ${Estructura.Sesion.TABLE_NAME} WHERE idUsuarioSesion = :idUsuario")
    fun eliminarSesion(idUsuario:Int): Int
}