package com.example.codigoverde.model

import java.util.Calendar

/**
 * Representa los tipos de comidas disponibles en el juego y su rango horario recomendado.
 */
enum class Comida(val titulo: String) {
    DESAYUNO("Desayuno"),
    ALMUERZO("Almuerzo"),
    MERIENDA("Merienda"),
    CENA("Cena");

    companion object {
        fun obtenerPorHora(hora: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)): Comida {
            return when (hora) {
                in 6..10 -> DESAYUNO
                in 11..14 -> ALMUERZO
                in 15..18 -> MERIENDA
                else -> CENA
            }
        }

        fun desdeTitulo(titulo: String?): Comida? {
            return entries.firstOrNull { it.titulo.equals(titulo, ignoreCase = true) }
        }
    }
}
