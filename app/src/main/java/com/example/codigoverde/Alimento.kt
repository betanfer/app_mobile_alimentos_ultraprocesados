package com.example.codigoverde

import java.io.Serializable

data class Alimento(
    val comida: String,
    val tipo: String,
    val opcion: String,
    val nivelSaludable: Int,
    val saciedad: Int,
    val vitalidad: Int,
    val imagen: String = "",
    val nombreCorto: String = ""
) : Serializable
