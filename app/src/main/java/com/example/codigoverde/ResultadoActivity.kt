package com.example.codigoverde

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.Avatar

class ResultadoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

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

