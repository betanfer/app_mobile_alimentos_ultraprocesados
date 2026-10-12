package com.example.codigoverde.model

/**
 * Constantes y reglas centrales del juego.
 */
object ReglasJuego {
    const val MAX_ALIMENTOS = 3
    const val TIEMPO_TOTAL_MS = 30000L
    const val INTERVALO_TIMER_MS = 1000L

    // Cada alimento otorga como máximo 2 salud + 2 saciedad + 2 vitalidad = 6 puntos.
    const val MAX_PUNTOS_POR_ALIMENTO = 6
    // Máximo por categoría (salud / saciedad / vitalidad): 2 puntos por cada uno de los 3 alimentos
    const val MAX_PUNTOS_POR_CATEGORIA = MAX_ALIMENTOS * 2 // 6
    const val MAXIMO_POSIBLE = MAX_ALIMENTOS * MAX_PUNTOS_POR_ALIMENTO // 18
    const val UMBRAL_VICTORIA = 0.60 // 60%
}
