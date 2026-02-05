package com.juandeherrera.reciclafacil.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

// definimos las claves
@Entity(
    tableName = Estructura.Historial.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = UsuarioData::class,
            parentColumns = [Estructura.Usuario.ID],
            childColumns = [Estructura.Historial.ID_USUARIO],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProductoData::class,
            parentColumns = [Estructura.Producto.ID],
            childColumns = [Estructura.Historial.ID_PRODUCTO],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistorialData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Estructura.Historial.ID)
    val idHistorial: Int = 0,

    @ColumnInfo(name = Estructura.Historial.ID_USUARIO)
    val idUsuarioSesion: Int,

    @ColumnInfo(name = Estructura.Historial.ID_PRODUCTO)
    val idProductoVisitado: Int
)