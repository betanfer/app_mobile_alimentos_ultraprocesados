package com.example.codigoverde.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.codigoverde.R
import com.example.codigoverde.model.ReglasJuego

/**
 * Indicador de puntaje de una categoría: [ReglasJuego.MAX_PUNTOS_POR_CATEGORIA] círculos
 * (los 6 puntos máximos de una categoría) que se llenan en orden.
 *
 * Es solo presentación: recibe el valor ya calculado por el ViewModel.
 */
class PuntajeCirculosView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    companion object {
        private const val TAMANO_DP = 28
        private const val MARGEN_DP = 4
        const val PASO_MS = 260L          // tiempo entre un círculo y el siguiente
        private const val POP_MS = 220L   // duración del "pop" de cada círculo
    }

    private val total = ReglasJuego.MAX_PUNTOS_POR_CATEGORIA
    private val circulos = List(total) { View(context) }

    init {
        orientation = HORIZONTAL
        val tamano = (TAMANO_DP * resources.displayMetrics.density).toInt()
        val margen = (MARGEN_DP * resources.displayMetrics.density).toInt()
        circulos.forEach { circulo ->
            circulo.layoutParams = LayoutParams(tamano, tamano).apply {
                setMargins(margen, margen, margen, margen)
            }
            circulo.setBackgroundResource(R.drawable.bg_score_dot_empty)
            addView(circulo)
        }
    }

    /** Muestra el puntaje directamente, sin animación. */
    fun mostrar(valor: Int) {
        cancelarAnimaciones()
        val llenos = valor.coerceIn(0, total)
        circulos.forEachIndexed { i, circulo -> pintar(circulo, i < llenos) }
        contentDescription = descripcion(llenos)
    }

    /**
     * Parte de todos vacíos y llena los círculos en orden, uno tras otro.
     * @param delayInicialMs espera antes de llenar el primero
     * @return duración total hasta terminar (para encadenar la siguiente categoría)
     */
    fun animar(valor: Int, delayInicialMs: Long): Long {
        cancelarAnimaciones()
        val llenos = valor.coerceIn(0, total)
        circulos.forEach { pintar(it, false) }
        contentDescription = descripcion(llenos)

        circulos.take(llenos).forEachIndexed { i, circulo ->
            circulo.animate()
                .setStartDelay(delayInicialMs + i * PASO_MS)
                .setDuration(POP_MS)
                .scaleX(1.35f).scaleY(1.35f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        pintar(circulo, true)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        // Vuelve al tamaño normal (si no se canceló)
                        circulo.animate().setStartDelay(0).setDuration(POP_MS / 2)
                            .scaleX(1f).scaleY(1f).setListener(null).start()
                    }
                })
                .start()
        }
        return delayInicialMs + llenos * PASO_MS + POP_MS
    }

    /** Detiene las animaciones en curso y deja los círculos en su tamaño normal. */
    fun cancelarAnimaciones() {
        circulos.forEach {
            it.animate().cancel()
            it.scaleX = 1f
            it.scaleY = 1f
        }
    }

    override fun onDetachedFromWindow() {
        cancelarAnimaciones()
        super.onDetachedFromWindow()
    }

    private fun pintar(circulo: View, lleno: Boolean) {
        circulo.setBackgroundResource(
            if (lleno) R.drawable.bg_score_dot_filled else R.drawable.bg_score_dot_empty
        )
    }

    private fun descripcion(llenos: Int) = "$llenos de $total"
}
