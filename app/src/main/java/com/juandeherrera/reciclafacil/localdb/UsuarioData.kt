package com.juandeherrera.reciclafacil.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

// se indica que la clase es una entidad que corresponde a una tabla en la bd, accediendo por el nombre
// se indica que el email es un campo unico, creando el indice
@Entity(
    tableName = Estructura.Usuario.TABLE_NAME,
    indices = [Index(value = [Estructura.Usuario.EMAIL], unique = true)]
)
data class UsuarioData (
    // clave primaria autogenerada incremental cuyo valor inicial es 0
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Estructura.Usuario.ID)
    val idUsuario: Int = 0,

    // columna del nombre del usuario
    @ColumnInfo(name = Estructura.Usuario.NOMBRE)
    val nombreUsuario: String,

    // columna del email del usuario
    @ColumnInfo(name = Estructura.Usuario.EMAIL)
    val emailUsuario: String,

    // columna de la contraseña del usuario
    @ColumnInfo(name = Estructura.Usuario.PASSWORD)
    val passwordUsuario: String
)