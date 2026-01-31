package com.juandeherrera.reciclafacil.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// se indica que la clase es una entidad que corresponde a una tabla en la bd, accediendo por el nombre
// se crea la clave foránea con foreignKeys
@Entity(
    tableName = Estructura.Historial.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioData::class,                    // la clave foránea viene de la tabla de usuarios
            parentColumns = [Estructura.Usuario.ID],        // se define el campo de la tabla padre (usuario) que sera referenciado
            childColumns = [Estructura.Sesion.ID_USUARIO],  // se define el campo de esta tabla que contendra la FK
            onDelete = ForeignKey.CASCADE                   // si se elimina el usuario, las sesiones relacionadas a él, se borran
        ),
        ForeignKey(
            entity = ProductoData::class,                       // la clave foránea viene de la tabla de usuarios
            parentColumns = [Estructura.Producto.ID],           // se define el campo de la tabla padre (producto) que sera referenciado
            childColumns = [Estructura.Historial.ID_PRODUCTO],   // se define el campo de esta tabla que contendra la FK
            onDelete = ForeignKey.CASCADE                       // si se elimina el producto, las sesiones relacionadas a él, se borran
        ),
    ]
)
data class HistorialData (
    // clave primaria autogenerada incremental cuyo valor inicial es 0
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Estructura.Historial.ID)
    val idSesion: Int = 0,

    // columna del id del usuario
    @ColumnInfo(name = Estructura.Historial.ID_USUARIO)
    val idUsuarioSesion: Int,

    // columna del id del producto visitado
    @ColumnInfo(name = Estructura.Historial.ID_PRODUCTO)
    val idProductoVisitado: Int
)