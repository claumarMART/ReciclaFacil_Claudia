package com.juandeherrera.reciclafacil.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// OPERACIONES QUE SE REALIZARAN EN LA TABLA USUARIO
@Dao
interface ProductoDAO {
    // OBTENER TODOS LOS PRODUCTOS
    @Query("SELECT * FROM ${Estructura.Producto.TABLE_NAME}")
    fun getListaProductos(): List<ProductoData>

    // OBTENER UN PRODUCTO SEGUN SU TITULO
    // se usan parametros (:nombreProducto) para evitar inyeccion SQL y que el parametro sea gestionado a través de la funcion
    // se usa ? para comprobar si el resultado es null antes de usarlo
    @Query("SELECT * FROM ${Estructura.Producto.TABLE_NAME} WHERE idProducto = :idProducto")
    fun getProducto(idProducto: Int): ProductoData?

    // INSERTAR UNA LISTA DE PRODUCTOS
    @Insert
    fun insertarProductos(productos: List<ProductoData>)
}