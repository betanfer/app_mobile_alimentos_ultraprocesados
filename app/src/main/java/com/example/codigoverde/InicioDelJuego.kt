package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InicioDelJuego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciodeljuego)

        val mainView = findViewById<View>(R.id.main)
        val baseMargin = resources.getDimensionPixelSize(R.dimen.screen_margin_horizontal)

        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + baseMargin,
                systemBars.top + baseMargin,
                systemBars.right + baseMargin,
                systemBars.bottom + baseMargin
            )
            insets
        }

        // 1. Botón Volver (<)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            SoundManager.playBubbly(this)
            finish() // Cierra la pantalla actual y vuelve
        }

        // 2. Botón ¡JUGAR! -> Navega a Paso1AvatarActivity (creación del avatar)
        val btnStartGame = findViewById<Button>(R.id.btnStartGame)
        btnStartGame.setOnClickListener {
            SoundManager.playStartGame(this)
            val intent = Intent(this, Paso1AvatarActivity::class.java)
            startActivity(intent)
        }
    }
}
