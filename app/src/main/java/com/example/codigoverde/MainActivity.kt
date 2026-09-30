package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnInicio = findViewById<View>(R.id.btnBadge)

        btnInicio.setOnClickListener {
            // Abre directamente la pantalla del juego
            val intent = Intent(this, InicioDelJuego::class.java)
            startActivity(intent)
        }
    }
}