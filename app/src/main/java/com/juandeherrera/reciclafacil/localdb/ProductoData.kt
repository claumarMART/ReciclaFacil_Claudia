package com.juandeherrera.reciclafacil.localdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// se indica que la clase es una entidad que corresponde a una tabla en la bd, accediendo por el nombre
// se indica que el email es un campo unico, creando el indice
@Entity(
    tableName = Estructura.Producto.TABLE_NAME
)
data class ProductoData (
    // clave primaria autogenerada incremental cuyo valor inicial es 0
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Estructura.Producto.ID)
    val idProducto: Int = 0,

    // columna del titulo del producto
    @ColumnInfo(name = Estructura.Producto.TITULO)
    val tituloProducto: String,

    // columna de la descripcion del producto
    @ColumnInfo(name = Estructura.Producto.DESCRIPCION)
    val descripcionProducto: String,

    // columna del contenedor del producto
    @ColumnInfo(name = Estructura.Producto.CONTENEDOR)
    val contenedorProducto: String
)