package com.example.codigoverde.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.R
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.model.ResultadoPartida
import com.example.codigoverde.util.SoundManager
import com.example.codigoverde.util.ThemeUtils
import com.example.codigoverde.viewmodel.ResultadoViewModel

class ResultadoActivity : AppCompatActivity() {

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT
    private val viewModel: ResultadoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)
        ThemeUtils.updateActivityColors(this)

        val resultadoPartida = intent.getSerializableExtra("EXTRA_RESULTADO") as? ResultadoPartida
        val avatar = intent.getSerializableExtra("EXTRA_AVATAR") as? Avatar

        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        viewModel.textoResumen.observe(this) { texto ->
            tvResultado.text = texto
        }

        viewModel.eventoSonido.observe(this) { evento ->
            evento.getContentIfNotHandled()?.let { esPositivo ->
                window.decorView.postDelayed({
                    if (isFinishing || isDestroyed) return@postDelayed
                    if (esPositivo) {
                        SoundManager.playResultPositive(this)
                    } else {
                        SoundManager.playResultNegative(this)
                    }
                }, 300)
            }
        }

        val btnSettings = findViewById<View>(R.id.btnSettings)
        btnSettings?.setOnClickListener {
            SoundManager.playBubbly(this)
            startActivity(Intent(this, menuSettings::class.java))
        }

        viewModel.cargarResultado(resultadoPartida, avatar)
    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.updateActivityColors(this)
        if (currentTheme != ThemeUtils.getSelectedTheme(this)) {
            recreate()
        }
    }
}
