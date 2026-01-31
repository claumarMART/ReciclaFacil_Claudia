package com.juandeherrera.reciclafacil.metodosAuxiliares

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.juandeherrera.reciclafacil.R

@Composable
fun obtenerPainterProducto(tituloProducto: String): Painter {

    var id: Int = 0

    // obtengo el id del drawable segun su titulo correspondiente
    when (tituloProducto){
        "Pilas" ->
            id = R.drawable.pilas
        "Botellas de plástico" ->
            id = R.drawable.botellasplastico
        "Papel y cartón" ->
            id = R.drawable.papelcarton
        "Residuos orgánicos" ->
            id = R.drawable.residuosorganicos
        "Tetrabricks" ->
            id = R.drawable.tetrabricks
    }

    return painterResource(id = id)
}