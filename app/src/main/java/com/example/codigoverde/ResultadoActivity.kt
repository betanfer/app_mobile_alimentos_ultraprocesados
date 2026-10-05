package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {

    companion object {
        // En una partida se eligen hasta 3 alimentos. Cada uno otorga hasta 6 pts (2 salud + 2 saciedad + 2 vitalidad).
        const val MAXIMO_POSIBLE = 18
        const val UMBRAL_60_PORCIENTO = 0.60
    }

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)
        ThemeUtils.updateActivityColors(this)

        val avatar = intent.getSerializableExtra("EXTRA_AVATAR") as Avatar
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Cálculo de puntajes y umbral del 60%
        val puntajeTotal = avatar.salud + avatar.saciedad + avatar.vitalidad
        val porcentajeObtenido = (puntajeTotal.toDouble() / MAXIMO_POSIBLE) * 100
        val esPositivo = puntajeTotal >= (MAXIMO_POSIBLE * UMBRAL_60_PORCIENTO)

        // Reproduce únicamente el sonido correspondiente (positivo si >= 60%, negativo si < 60%)
        window.decorView.postDelayed({
            if (isFinishing || isDestroyed) return@postDelayed
            if (esPositivo) {
                SoundManager.playResultPositive(this)
            } else {
                SoundManager.playResultNegative(this)
            }
        }, 300)

        val mensajeFinal = if (esPositivo) {
            "¡PARTIDA GANADA! 🎉\nExcelente combinación de alimentos saludables."
        } else {
            "¡PARTIDA PERDIDA! ⚠️\nCuidado con los ultraprocesados y calorías vacías."
        }

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
            Total: $puntajeTotal / $MAXIMO_POSIBLE pts (${String.format("%.1f", porcentajeObtenido)}%)
            
            ---------------------------
            EVALUACIÓN:
            ---------------------------
            $mensajeFinal
        """.trimIndent()

        val btnSettings = findViewById<View>(R.id.btnSettings)
        btnSettings?.setOnClickListener {
            SoundManager.playBubbly(this)
            startActivity(Intent(this, menuSettings::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.updateActivityColors(this)
        if (currentTheme != ThemeUtils.getSelectedTheme(this)) {
            recreate()
        }
    }
}
