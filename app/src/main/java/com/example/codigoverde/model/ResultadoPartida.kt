package com.example.codigoverde.model

import java.io.Serializable

/**
 * Representa el resultado obtenido al finalizar una partida de elección de alimentos.
 */
data class ResultadoPartida(
    val avatar: Avatar,
    val saludGanada: Int,
    val saciedadGanada: Int,
    val vitalidadGanada: Int,
    val puntajeTotal: Int,
    val maximoPosible: Int = ReglasJuego.MAXIMO_POSIBLE,
    val porcentajeObtenido: Double = if (maximoPosible > 0) (puntajeTotal.toDouble() / maximoPosible) * 100 else 0.0,
    val esVictoria: Boolean = puntajeTotal >= (maximoPosible * ReglasJuego.UMBRAL_VICTORIA)
) : Serializable
