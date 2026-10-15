package com.example.codigoverde.util

/**
 * Envoltorio para eventos emitidos por LiveData que solo deben procesarse una única vez.
 * Evita que al rotar la pantalla o recrear la vista se vuelva a disparar un Toast, sonido o navegación.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * Retorna el contenido y marca el evento como manejado.
     * Retorna null si ya había sido consumido previamente.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Retorna el contenido sin modificar el estado de manejado.
     */
    fun peekContent(): T = content
}
