package com.example.codigoverde

import java.io.Serializable
data class Avatar(
    val nombre: String,
    val sexo: String,
    val edad: Int,
    var salud: Int = 0,     // Nivel inicial 0%
    var saciedad: Int = 0,  // Nivel inicial 0%
    var vitalidad: Int = 0 // Nivel inicial 0%
) : Serializable


