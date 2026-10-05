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

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciodeljuego)
        ThemeUtils.updateActivityColors(this)

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

        // 2. Botón ¡JUGAR! -> Si ya existe avatar guardado, avanza directamente a SeleccionarMenuActivity
        val btnStartGame = findViewById<Button>(R.id.btnStartGame)
        btnStartGame.setOnClickListener {
            SoundManager.playStartGame(this)
            val avatarGuardado = UserPreferences.obtenerAvatar(this)
            if (avatarGuardado != null) {
                val intent = Intent(this, SeleccionarMenuActivity::class.java).apply {
                    putExtra("EXTRA_AVATAR", avatarGuardado)
                }
                startActivity(intent)
            } else {
                val intent = Intent(this, Paso1AvatarActivity::class.java)
                startActivity(intent)
            }
        }

        // 3. Botón Configuración
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
