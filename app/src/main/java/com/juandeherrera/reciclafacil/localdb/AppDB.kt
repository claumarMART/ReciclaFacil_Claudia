package com.juandeherrera.reciclafacil.localdb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UsuarioData::class, SesionData::class, ProductoData::class, HistorialData::class],
    version = 2, // Subimos a versión 2
    exportSchema = true
)
abstract class AppDB : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun sesionDao(): SesionDAO
    abstract fun productoDao(): ProductoDAO
    abstract fun historialDao(): HistorialDAO
}