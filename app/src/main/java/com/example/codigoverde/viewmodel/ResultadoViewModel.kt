package com.example.codigoverde.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.codigoverde.data.DatoCuriosoRepository
import com.example.codigoverde.model.Alimento
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.model.DatoCurioso
import com.example.codigoverde.model.ReglasJuego
import com.example.codigoverde.model.ResultadoPartida
import com.example.codigoverde.util.Event
import kotlin.random.Random

/**
 * Estado de la pantalla de resultados, listo para que la vista lo dibuje.
 * Los puntajes por categoría ya vienen acotados a 0..[ReglasJuego.MAX_PUNTOS_POR_CATEGORIA]
 * (los alimentos no saludables pueden restar, pero la vista solo muestra círculos llenos).
 */
data class PuntajeUi(
    val salud: Int,
    val saciedad: Int,
    val vitalidad: Int,
    val total: Int,
    val maximo: Int,
    val porcentaje: Double,
    val esVictoria: Boolean
)

/**
 * ViewModel para la pantalla de Resultados.
 * Encapsula la evaluación de los resultados de la partida:
 * - Puntaje por categoría, total, porcentaje y estado de victoria/derrota.
 * - Evento de sonido según el resultado.
 * - Dato curioso aleatorio de uno de los alimentos elegidos.
 * - Avatar "limpio" para volver a jugar.
 */
class ResultadoViewModel(application: Application) : AndroidViewModel(application) {

    private val _resultado = MutableLiveData<ResultadoPartida>()
    val resultado: LiveData<ResultadoPartida> = _resultado

    private val _puntaje = MutableLiveData<PuntajeUi>()
    val puntaje: LiveData<PuntajeUi> = _puntaje

    private val _eventoSonido = MutableLiveData<Event<Boolean>>() // true = positivo, false = negativo
    val eventoSonido: LiveData<Event<Boolean>> = _eventoSonido

    // null = no hay dato para mostrar (la vista oculta la tarjeta)
    private val _datoCurioso = MutableLiveData<DatoCurioso?>()
    val datoCurioso: LiveData<DatoCurioso?> = _datoCurioso

    // El sorteo se hace una sola vez: el ViewModel sobrevive a la rotación y el dato no cambia
    private var datoYaSorteado = false

    // La animación de llenado de círculos se muestra una sola vez (no se repite al rotar)
    private var animacionMostrada = false

    fun cargarResultado(resultadoPartida: ResultadoPartida?, avatarFallback: Avatar?) {
        // Evita recalcular (y volver a disparar el sonido) si la vista se recrea
        if (_resultado.value != null) return

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

        val maxCategoria = ReglasJuego.MAX_PUNTOS_POR_CATEGORIA
        _puntaje.value = PuntajeUi(
            salud = res.saludGanada.coerceIn(0, maxCategoria),
            saciedad = res.saciedadGanada.coerceIn(0, maxCategoria),
            vitalidad = res.vitalidadGanada.coerceIn(0, maxCategoria),
            total = res.puntajeTotal.coerceAtLeast(0),
            maximo = res.maximoPosible,
            porcentaje = res.porcentajeObtenido.coerceAtLeast(0.0),
            esVictoria = res.esVictoria
        )

        _eventoSonido.value = Event(res.esVictoria)

        if (!datoYaSorteado) {
            datoYaSorteado = true
            val datos = DatoCuriosoRepository.cargarDatos(getApplication())
            _datoCurioso.value = elegirDatoCurioso(res.alimentosElegidos, datos)
        }
    }

    /** true solo la primera vez: indica si la vista debe animar el llenado de círculos. */
    fun consumirAnimacion(): Boolean {
        if (animacionMostrada) return false
        animacionMostrada = true
        return true
    }

    /**
     * Avatar con los datos del jugador y los puntajes en cero, para empezar una partida nueva.
     * (El avatar de la partida terminada trae los puntajes acumulados.)
     */
    fun avatarParaNuevaPartida(): Avatar? {
        val av = _resultado.value?.avatar ?: return null
        return av.copy(salud = 0, saciedad = 0, vitalidad = 0)
    }

    companion object {
        /**
         * Elige al azar un dato curioso entre los alimentos elegidos.
         * Ignora los alimentos sin dato. Devuelve null si no hay ninguno disponible.
         */
        fun elegirDatoCurioso(
            alimentos: List<Alimento>,
            datos: Map<String, DatoCurioso>,
            random: Random = Random.Default
        ): DatoCurioso? {
            return alimentos.mapNotNull { datos[it.opcion] }.randomOrNull(random)
        }
    }
}
