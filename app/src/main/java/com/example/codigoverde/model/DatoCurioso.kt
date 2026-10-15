package com.example.codigoverde.model

import java.io.Serializable

/**
 * Dato curioso asociado a un plato del menú.
 * [plato] coincide con [Alimento.opcion].
 */
data class DatoCurioso(
    val plato: String,
    val dato: String
) : Serializable
