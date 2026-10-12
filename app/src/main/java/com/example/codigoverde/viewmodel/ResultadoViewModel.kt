package com.example.codigoverde.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.model.ResultadoPartida
import com.example.codigoverde.util.Event

/**
 * ViewModel para la pantalla de Resultados.
 * Encapsula la evaluación de los resultados de la partida:
 * - Puntaje final, porcentaje, estado de victoria/derrota.
 * - Formato del texto del resumen del jugador.
 * - Evento de sonido según el resultado.
 */
class ResultadoViewModel : ViewModel() {

    private val _resultado = MutableLiveData<ResultadoPartida>()
    val resultado: LiveData<ResultadoPartida> = _resultado

    private val _textoResumen = MutableLiveData<String>()
    val textoResumen: LiveData<String> = _textoResumen

    private val _eventoSonido = MutableLiveData<Event<Boolean>>() // true = positivo, false = negativo
    val eventoSonido: LiveData<Event<Boolean>> = _eventoSonido

    fun cargarResultado(resultadoPartida: ResultadoPartida?, avatarFallback: Avatar?) {
        val res = resultadoPartida ?: run {
            val av = avatarFallback ?: Avatar("", "", 0)
            val total = av.salud + av.saciedad + av.vitalidad
            ResultadoPartida(
                avatar = av,
                saludGanada = av.salud,
                saciedadGanada = av.saciedad,
                vitalidadGanada = av.vitalidad,
                puntajeTotal = total
            )
        }

        _resultado.value = res

        val mensajeFinal = if (res.esVictoria) {
            "¡PARTIDA GANADA! 🎉\nExcelente combinación de alimentos saludables."
        } else {
            "¡PARTIDA PERDIDA! ⚠️\nCuidado con los ultraprocesados y calorías vacías."
        }

        _textoResumen.value = """
            ===========================
               TARJETA DEL JUGADOR
            ===========================
            Nombre: ${res.avatar.nombre}
            Sexo: ${res.avatar.sexo}
            Edad: ${res.avatar.edad} años
            
            ---------------------------
            PUNTAJE FINAL OBTENIDO:
            ---------------------------
            Salud: ${res.avatar.salud} pts
            Saciedad: ${res.avatar.saciedad} pts
            Vitalidad: ${res.avatar.vitalidad} pts
            Total: ${res.puntajeTotal} / ${res.maximoPosible} pts (${String.format("%.1f", res.porcentajeObtenido)}%)
            
            ---------------------------
            EVALUACIÓN:
            ---------------------------
            $mensajeFinal
        """.trimIndent()

        _eventoSonido.value = Event(res.esVictoria)
    }
}
