package com.example.codigoverde

import android.content.Context

/**
 * Resuelve la imagen en res/drawable y el texto a mostrar para un alimento.
 * Si el alimento no tiene imagen asignada o el recurso no existe aún,
 * utiliza el logo como fallback.
 */
object AlimentoVisual {

    fun imagenRes(context: Context, alimento: Alimento): Int {
        if (alimento.imagen.isNotEmpty()) {
            val id = context.resources.getIdentifier(alimento.imagen, "drawable", context.packageName)
            if (id != 0) return id
        }
        return R.drawable.logo_codigo_verde
    }

    fun nombreCorto(alimento: Alimento): String {
        return alimento.nombreCorto.ifEmpty { alimento.opcion }
    }
}
