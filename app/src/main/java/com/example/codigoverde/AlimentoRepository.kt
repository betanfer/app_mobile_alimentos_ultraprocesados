package com.example.codigoverde

import android.content.Context

object AlimentoRepository {
    fun cargarAlimentos(context: Context): List<Alimento> {
        val lista = mutableListOf<Alimento>()
        context.assets.open("menu_alimentos_puntuado.txt").bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line ->
                val cols = line.split("\t")
                if (cols.size >= 6) {
                    lista.add(
                        Alimento(
                            comida = cols[0].trim(),
                            tipo = cols[1].trim(),
                            opcion = cols[2].trim(),
                            nivelSaludable = cols[3].trim().toIntOrNull() ?: 0,
                            saciedad = cols[4].trim().toIntOrNull() ?: 0,
                            vitalidad = cols[5].trim().toIntOrNull() ?: 0
                        )
                    )
                }
            }
        }
        return lista
    }
}