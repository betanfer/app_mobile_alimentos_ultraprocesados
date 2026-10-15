package com.example.codigoverde.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.codigoverde.R
import com.example.codigoverde.data.UserPreferences
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.util.Event

/**
 * ViewModel encargado de la gestión del Avatar/Usuario:
 * - Carga y observación de los datos del perfil guardados en UserPreferences
 * - Validación de entradas (nombre y rango de edad)
 * - Persistencia del perfil
 * - Emisión de eventos únicos para la UI (toasts, navegación)
 */
class AvatarViewModel(application: Application) : AndroidViewModel(application) {

    private val _avatarActual = MutableLiveData<Avatar?>()
    val avatarActual: LiveData<Avatar?> = _avatarActual

    sealed class UiEvent {
        object PlayBubblySound : UiEvent()
        data class ShowToast(val resId: Int? = null, val mensaje: String? = null) : UiEvent()
        data class GuardadoExitoso(val avatar: Avatar, val esModoEdicion: Boolean) : UiEvent()
    }

    private val _eventoUi = MutableLiveData<Event<UiEvent>>()
    val eventoUi: LiveData<Event<UiEvent>> = _eventoUi

    init {
        cargarAvatar()
    }

    fun cargarAvatar() {
        _avatarActual.value = UserPreferences.obtenerAvatar(getApplication())
    }

    fun guardarPerfil(nombre: String, edadTexto: String, esMasculino: Boolean, esModoEdicion: Boolean) {
        val nombreTrim = nombre.trim()
        val edad = edadTexto.trim().toIntOrNull()

        if (nombreTrim.isEmpty() || edad == null || edad !in 1..120) {
            _eventoUi.value = Event(UiEvent.ShowToast(resId = R.string.error_datos_avatar))
            return
        }

        val sexo = if (esMasculino) Avatar.SEXO_MASCULINO else Avatar.SEXO_FEMENINO
        val nuevoAvatar = Avatar(nombreTrim, sexo, edad)

        UserPreferences.guardarAvatar(getApplication(), nuevoAvatar)
        _avatarActual.value = nuevoAvatar

        _eventoUi.value = Event(UiEvent.PlayBubblySound)
        _eventoUi.value = Event(UiEvent.GuardadoExitoso(nuevoAvatar, esModoEdicion))
    }
}
