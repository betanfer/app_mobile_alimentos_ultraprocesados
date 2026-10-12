package com.example.codigoverde.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.codigoverde.data.AlimentoRepository
import com.example.codigoverde.model.Alimento
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.model.ReglasJuego
import com.example.codigoverde.model.ResultadoPartida
import com.example.codigoverde.util.Event

/**
 * ViewModel para la pantalla de juego.
 * Gestiona:
 * - Lista de opciones de alimentos para la comida seleccionada
 * - Alimentos seleccionados en la bandeja
 * - Temporizador del juego (sobrevive rotación y pausa)
 * - Estado de fin de juego y cálculo del resultado
 * - Eventos de UI únicos (sonidos, avisos, navegación)
 */
class JuegoViewModel(application: Application) : AndroidViewModel(application) {

    // Lista de alimentos disponibles para el menú
    private val _alimentos = MutableLiveData<List<Alimento>>(emptyList())
    val alimentos: LiveData<List<Alimento>> = _alimentos

    // Alimentos seleccionados por el jugador en la bandeja
    private val _bandeja = MutableLiveData<List<Alimento>>(emptyList())
    val bandeja: LiveData<List<Alimento>> = _bandeja

    // Tiempo restante en milisegundos
    private val _tiempoRestanteMs = MutableLiveData<Long>(ReglasJuego.TIEMPO_TOTAL_MS)
    val tiempoRestanteMs: LiveData<Long> = _tiempoRestanteMs

    // Si la partida ya finalizó
    private val _partidaTerminada = MutableLiveData<Boolean>(false)
    val partidaTerminada: LiveData<Boolean> = _partidaTerminada

    // Eventos únicos para la UI
    sealed class UiEvent {
        object PlayBubblySound : UiEvent()
        object PlayUrgencySound : UiEvent()
        object PlayErrorSound : UiEvent()
        data class ShowToast(val mensaje: String) : UiEvent()
        data class FinalizarPartida(val resultado: ResultadoPartida) : UiEvent()
    }

    private val _eventoUi = MutableLiveData<Event<UiEvent>>()
    val eventoUi: LiveData<Event<UiEvent>> = _eventoUi

    private var timer: CountDownTimer? = null
    private var isTimerRunning = false
    private var avatarActual: Avatar? = null
    private var comidaActual: String = ""

    /**
     * Inicializa los datos de la partida si aún no se han cargado.
     */
    fun iniciarJuego(avatar: Avatar, comida: String) {
        this.avatarActual = avatar
        if (_alimentos.value.isNullOrEmpty() || comidaActual != comida) {
            this.comidaActual = comida
            val lista = AlimentoRepository.cargarAlimentos(getApplication()).filter {
                it.comida.equals(comida, ignoreCase = true)
            }
            _alimentos.value = lista
            _bandeja.value = emptyList()
            _tiempoRestanteMs.value = ReglasJuego.TIEMPO_TOTAL_MS
            _partidaTerminada.value = false
        }
    }

    /**
     * Inicia o reanuda el temporizador con el tiempo restante.
     */
    fun reanudarTimer() {
        if (_partidaTerminada.value == true || isTimerRunning) return

        val tiempo = _tiempoRestanteMs.value ?: ReglasJuego.TIEMPO_TOTAL_MS
        if (tiempo <= 0) {
            procesarFinal()
            return
        }

        timer?.cancel()
        timer = object : CountDownTimer(tiempo, ReglasJuego.INTERVALO_TIMER_MS) {
            override fun onTick(millisUntilFinished: Long) {
                _tiempoRestanteMs.value = millisUntilFinished
                val segundosRestantes = (millisUntilFinished / 1000).toInt()
                if (segundosRestantes in 1..5) {
                    _eventoUi.value = Event(UiEvent.PlayUrgencySound)
                }
            }

            override fun onFinish() {
                _tiempoRestanteMs.value = 0L
                procesarFinal()
            }
        }.start()
        isTimerRunning = true
    }

    /**
     * Pausa el temporizador cuando la vista pasa a segundo plano.
     */
    fun pausarTimer() {
        timer?.cancel()
        timer = null
        isTimerRunning = false
    }

    /**
     * Alterna la selección de un alimento (agrega o quita).
     */
    fun alternarAlimento(alimento: Alimento) {
        if (_partidaTerminada.value == true) return

        val listaActual = _bandeja.value.orEmpty().toMutableList()
        if (listaActual.contains(alimento)) {
            listaActual.remove(alimento)
            _bandeja.value = listaActual
            _eventoUi.value = Event(UiEvent.PlayBubblySound)
        } else if (listaActual.size < ReglasJuego.MAX_ALIMENTOS) {
            listaActual.add(alimento)
            _bandeja.value = listaActual
            _eventoUi.value = Event(UiEvent.PlayBubblySound)
        } else {
            _eventoUi.value = Event(UiEvent.PlayErrorSound)
            _eventoUi.value = Event(UiEvent.ShowToast("Máximo ${ReglasJuego.MAX_ALIMENTOS} alimentos"))
        }
    }

    /**
     * Confirma la selección del menú y finaliza la partida.
     */
    fun confirmarSeleccion() {
        if (_partidaTerminada.value == true) return
        val lista = _bandeja.value.orEmpty()
        if (lista.isEmpty()) {
            _eventoUi.value = Event(UiEvent.ShowToast("Elegí al menos un alimento"))
            return
        }
        procesarFinal()
    }

    /**
     * Cancela la partida anticipadamente (por ejemplo, si el usuario presiona la X).
     */
    fun cancelarPartida() {
        _partidaTerminada.value = true
        pausarTimer()
    }

    /**
     * Calcula los puntajes de la partida y emite el evento de finalización.
     */
    private fun procesarFinal() {
        if (_partidaTerminada.value == true) return
        _partidaTerminada.value = true
        pausarTimer()

        val avatar = avatarActual ?: Avatar("", "", 0)
        var saludGanada = 0
        var saciedadGanada = 0
        var vitalidadGanada = 0

        for (item in _bandeja.value.orEmpty()) {
            saludGanada += item.nivelSaludable
            saciedadGanada += item.saciedad
            vitalidadGanada += item.vitalidad
        }

        avatar.salud += saludGanada
        avatar.saciedad += saciedadGanada
        avatar.vitalidad += vitalidadGanada

        val total = saludGanada + saciedadGanada + vitalidadGanada
        val resultado = ResultadoPartida(
            avatar = avatar,
            saludGanada = saludGanada,
            saciedadGanada = saciedadGanada,
            vitalidadGanada = vitalidadGanada,
            puntajeTotal = total,
            alimentosElegidos = _bandeja.value.orEmpty()
        )

        _eventoUi.value = Event(UiEvent.FinalizarPartida(resultado))
    }

    override fun onCleared() {
        super.onCleared()
        pausarTimer()
    }
}
