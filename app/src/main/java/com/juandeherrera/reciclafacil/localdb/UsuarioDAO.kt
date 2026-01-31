package com.juandeherrera.reciclafacil.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// OPERACIONES QUE SE REALIZARAN EN LA TABLA USUARIO
@Dao
interface UsuarioDAO {
    // OBTENER UN USUARIO SEGUN SU EMAIL
    // se usan parametros (:email) para evitar inyeccion SQL y que el parametro sea gestionado a través de la funcion
    // se usa ? para comprobar si el resultado es null antes de usarlo
    @Query("SELECT * FROM ${Estructura.Usuario.TABLE_NAME} WHERE emailUsuario = :email")
    fun getUser(email:String): UsuarioData?

    // AGREGAR UN NUEVO USUARIO
    @Insert
    fun nuevoUsuario (usuarioData: UsuarioData)

    // ACTUALIZAR UN USUARIO EXISTENTE
    @Update
    fun actualizarUsuario(usuarioData: UsuarioData)

    // ELIMINAR UN USUARIO EXISTE A PARTIR DE SU ID
    // se usan parametros (:idUsuario) para evitar inyeccion SQL y que el parametro sea gestionado a través de la funcion
    // si no habia sesion devuelve 0, pero si la elimino correctamente devolverá 1
    @Query("DELETE FROM ${Estructura.Usuario.TABLE_NAME} WHERE idUsuario = :idUsuario")
    fun eliminarUsuario(idUsuario:Int): Int
}