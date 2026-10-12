package com.example.codigoverde.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
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

        val imgResultadoEstado = findViewById<ImageView>(R.id.imgResultadoEstado)
        val tvResultadoTitulo = findViewById<TextView>(R.id.tvResultadoTitulo)
        val tvResultadoSubtitulo = findViewById<TextView>(R.id.tvResultadoSubtitulo)

        val dotsSalud = findViewById<PuntajeCirculosView>(R.id.dotsSalud)
        val dotsSaciedad = findViewById<PuntajeCirculosView>(R.id.dotsSaciedad)
        val dotsVitalidad = findViewById<PuntajeCirculosView>(R.id.dotsVitalidad)

        val tvPuntosSalud = findViewById<TextView>(R.id.tvPuntosSalud)
        val tvPuntosSaciedad = findViewById<TextView>(R.id.tvPuntosSaciedad)
        val tvPuntosVitalidad = findViewById<TextView>(R.id.tvPuntosVitalidad)
        val tvPuntajeTotal = findViewById<TextView>(R.id.tvPuntajeTotal)

        val cardDatoCurioso = findViewById<View>(R.id.cardDatoCurioso)
        val tvPlatoDatoCurioso = findViewById<TextView>(R.id.tvPlatoDatoCurioso)
        val tvDatoCurioso = findViewById<TextView>(R.id.tvDatoCurioso)

        val btnVolverAJugar = findViewById<Button>(R.id.btnVolverAJugar)
        val btnSettings = findViewById<View>(R.id.btnSettings)

        viewModel.puntaje.observe(this) { puntaje ->
            if (puntaje.esVictoria) {
                imgResultadoEstado.setImageResource(R.drawable.img_resultado_victoria)
                tvResultadoTitulo.text = getString(R.string.titulo_resultado_ganada)
                tvResultadoSubtitulo.text = getString(R.string.subtitulo_resultado_ganada)
            } else {
                imgResultadoEstado.setImageResource(R.drawable.img_resultado_derrota)
                tvResultadoTitulo.text = getString(R.string.titulo_resultado_perdida)
                tvResultadoSubtitulo.text = getString(R.string.subtitulo_resultado_perdida)
            }

            tvPuntosSalud.text = "${puntaje.salud} pts"
            tvPuntosSaciedad.text = "${puntaje.saciedad} pts"
            tvPuntosVitalidad.text = "${puntaje.vitalidad} pts"
            tvPuntajeTotal.text = "${puntaje.total} / ${puntaje.maximo} pts (${puntaje.porcentaje.toInt()}%)"

            if (viewModel.consumirAnimacion()) {
                val delaySaciedad = dotsSalud.animar(puntaje.salud, 200L)
                val delayVitalidad = dotsSaciedad.animar(puntaje.saciedad, delaySaciedad)
                dotsVitalidad.animar(puntaje.vitalidad, delayVitalidad)
            } else {
                dotsSalud.mostrar(puntaje.salud)
                dotsSaciedad.mostrar(puntaje.saciedad)
                dotsVitalidad.mostrar(puntaje.vitalidad)
            }
        }

        viewModel.datoCurioso.observe(this) { dato ->
            if (dato == null) {
                cardDatoCurioso.visibility = View.GONE
            } else {
                tvPlatoDatoCurioso.text = dato.plato
                tvDatoCurioso.text = dato.dato
                cardDatoCurioso.visibility = View.VISIBLE
            }
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

        btnVolverAJugar.setOnClickListener {
            SoundManager.playBubbly(this)
            val avatarNuevo = viewModel.avatarParaNuevaPartida() ?: Avatar("", "", 0)
            val intent = Intent(this, SeleccionarMenuActivity::class.java).apply {
                putExtra("EXTRA_AVATAR", avatarNuevo)
            }
            startActivity(intent)
            finish()
        }

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
