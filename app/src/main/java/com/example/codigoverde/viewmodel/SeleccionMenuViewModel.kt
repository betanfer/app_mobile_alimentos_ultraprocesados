package com.example.codigoverde.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codigoverde.model.Comida
import com.example.codigoverde.util.Event

/**
 * ViewModel para la pantalla de Selección de Menú.
 * Gestiona:
 * - Modo automático vs manual ("Yo elijo")
 * - Comida sugerida según horario
 * - Comida seleccionada
 * - Eventos de UI: sonidos, validaciones y navegación al juego
 */
class SeleccionMenuViewModel : ViewModel() {

    private val _modoAutomatico = MutableLiveData<Boolean>(true)
    val modoAutomatico: LiveData<Boolean> = _modoAutomatico

    private val _comidaElegida = MutableLiveData<Comida?>(null)
    val comidaElegida: LiveData<Comida?> = _comidaElegida

    private val _comidaHorarioActual = MutableLiveData<Comida>()
    val comidaHorarioActual: LiveData<Comida> = _comidaHorarioActual

    sealed class UiEvent {
        object PlayBubblySound : UiEvent()
        data class ShowToast(val mensaje: String) : UiEvent()
        data class IniciarJuego(val comidaTitulo: String) : UiEvent()
    }

    private val _eventoUi = MutableLiveData<Event<UiEvent>>()
    val eventoUi: LiveData<Event<UiEvent>> = _eventoUi

    init {
        actualizarHorarioActual()
    }

    fun actualizarHorarioActual() {
        _comidaHorarioActual.value = Comida.obtenerPorHora()
    }

    fun seleccionarModoAutomatico() {
        _modoAutomatico.value = true
        _eventoUi.value = Event(UiEvent.PlayBubblySound)
    }

    fun seleccionarModoManual() {
        _modoAutomatico.value = false
        _eventoUi.value = Event(UiEvent.PlayBubblySound)
    }

    fun seleccionarComida(comida: Comida) {
        _modoAutomatico.value = false
        _comidaElegida.value = comida
        _eventoUi.value = Event(UiEvent.PlayBubblySound)
    }

    fun confirmarSeleccion() {
        val comidaFinal = if (_modoAutomatico.value == true) {
            _comidaHorarioActual.value ?: Comida.obtenerPorHora()
        } else {
            _comidaElegida.value
        }

        if (comidaFinal == null) {
            _eventoUi.value = Event(UiEvent.ShowToast("Elegí una comida para continuar"))
            return
        }

        _eventoUi.value = Event(UiEvent.PlayBubblySound)
        _eventoUi.value = Event(UiEvent.IniciarJuego(comidaFinal.titulo))
    }
}
