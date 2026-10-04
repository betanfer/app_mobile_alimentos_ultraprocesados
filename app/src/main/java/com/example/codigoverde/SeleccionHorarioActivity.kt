package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.Avatar
class SeleccionHorarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_horario)

        val avatar = intent.getSerializableExtra("EXTRA_AVATAR") as Avatar
        val spinner = findViewById<Spinner>(R.id.spinnerHorarios)

        val opciones = arrayOf(
            "De 06 a 10 hs - DESAYUNO",
            "De 11 a 14 hs - ALMUERZO",
            "De 15 a 18 hs - MERIENDA",
            "De 19 a 23 hs - CENA"
        )

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)

        findViewById<Button>(R.id.btnContinuar).setOnClickListener {
            val comida = when (spinner.selectedItemPosition) {
                0 -> "Desayuno"
                1 -> "Almuerzo"
                2 -> "Merienda"
                else -> "Cena"
            }

            val intent = Intent(this, JuegoBandejaActivity::class.java).apply {
                putExtra("EXTRA_AVATAR", avatar)
                putExtra("COMIDA_SELECCIONADA", comida)
            }
            startActivity(intent)
        }
    }
}