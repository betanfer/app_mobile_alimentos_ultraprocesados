package com.example.codigoverde.model

import java.io.Serializable

data class Avatar(
    val nombre: String,
    val sexo: String,
    val edad: Int,
    var salud: Int = 0,     // Nivel inicial 0%
    var saciedad: Int = 0,  // Nivel inicial 0%
    var vitalidad: Int = 0 // Nivel inicial 0%
) : Serializable {
    companion object {
        const val SEXO_MASCULINO = "Masculino"
        const val SEXO_FEMENINO = "Femenino"
    }
}
