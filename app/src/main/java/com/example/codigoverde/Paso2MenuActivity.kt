package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.Avatar
import java.util.Calendar

class Paso2MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso2_menu)

        val avatar = intent.getSerializableExtra("EXTRA_AVATAR") as Avatar

        findViewById<Button>(R.id.btnOpcionA).setOnClickListener {
            val intent = Intent(this, SeleccionHorarioActivity::class.java).apply {
                putExtra("EXTRA_AVATAR", avatar)
            }
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnOpcionB).setOnClickListener {
            val comidaAutomatica = obtenerComidaPorHoraActual()
            val intent = Intent(this, JuegoBandejaActivity::class.java).apply {
                putExtra("EXTRA_AVATAR", avatar)
                putExtra("COMIDA_SELECCIONADA", comidaAutomatica)
            }
            startActivity(intent)
        }
    }

    private fun obtenerComidaPorHoraActual(): String {
        val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hora) {
            in 6..10 -> "Desayuno"
            in 11..14 -> "Almuerzo"
            in 15..18 -> "Merienda"
            else -> "Cena"
        }
    }
}