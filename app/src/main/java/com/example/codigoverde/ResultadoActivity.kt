package com.example.codigoverde

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.Avatar
import kotlin.random.Random

class ResultadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        // Reproduce de forma aleatoria (positivo o negativo) hasta que se definan los puntajes.
        // Se retrasa para que no se superponga con el sonido de fin de partida.
        window.decorView.postDelayed({
            if (isFinishing || isDestroyed) return@postDelayed
            if (Random.nextBoolean()) {
                SoundManager.playResultPositive(this)
            } else {
                SoundManager.playResultNegative(this)
            }
        }, 1300)

        val avatar = intent.getSerializableExtra("EXTRA_AVATAR") as Avatar
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        tvResultado.text = """
            ===========================
               TARJETA DEL JUGADOR
            ===========================
            Nombre: ${avatar.nombre}
            Sexo: ${avatar.sexo}
            Edad: ${avatar.edad} años
            
            ---------------------------
            PUNTAJE FINAL OBTENIDO:
            ---------------------------
            Salud: ${avatar.salud} pts
            Saciedad: ${avatar.saciedad} pts
            Vitalidad: ${avatar.vitalidad} pts
        """.trimIndent()
    }
}

