package com.example.codigoverde

import java.io.Serializable

data class Alimento(
    val comida: String,
    val tipo: String,
    val opcion: String,
    val nivelSaludable: Int,
    val saciedad: Int,
    val vitalidad: Int,
    // Número de fila en el archivo (1..40). Se usa para asociar la imagen y el nombre corto.
    val numero: Int = 0
) : Serializable
