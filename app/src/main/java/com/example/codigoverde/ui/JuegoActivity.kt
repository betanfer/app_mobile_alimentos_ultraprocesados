package com.example.codigoverde.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.codigoverde.R
import com.example.codigoverde.model.Alimento
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.model.ReglasJuego
import com.example.codigoverde.util.AlimentoVisual
import com.example.codigoverde.util.SoundManager
import com.example.codigoverde.util.ThemeUtils
import com.example.codigoverde.viewmodel.JuegoViewModel

/**
 * Pantalla de juego (View en MVVM):
 * Representa la interfaz que utiliza el jugador.
 * Observa el estado expuesto por JuegoViewModel (bandeja, alimentos, temporizador, eventos)
 * y reacciona actualizando la UI y manejando la interacción táctil.
 */
class JuegoActivity : AppCompatActivity() {

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT
    private val viewModel: JuegoViewModel by viewModels()

    private val tarjetas = mutableMapOf<Alimento, View>()
    private lateinit var tvTimer: TextView
    private lateinit var tvBandeja: TextView
    private lateinit var gridAlimentos: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego)
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

        val avatar = intent.getSerializableExtra("EXTRA_AVATAR") as? Avatar ?: Avatar("", "", 0)
        val comida = intent.getStringExtra("COMIDA_SELECCIONADA") ?: "Desayuno"

        tvTimer = findViewById(R.id.tvTimer)
        tvBandeja = findViewById(R.id.tvBandeja)
        gridAlimentos = findViewById(R.id.gridAlimentos)
        findViewById<TextView>(R.id.tvGameTitle).text = "OPCIONES DE ${comida.uppercase()}"

        // Botón Cruz (X) -> Regresa a la primera pantalla
        val btnClose = findViewById<ImageButton>(R.id.btnBack2)
        btnClose?.setOnClickListener {
            SoundManager.playBubbly(this)
            viewModel.cancelarPartida()
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // Botón Configuración
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        btnSettings?.setOnClickListener {
            SoundManager.playBubbly(this)
            startActivity(Intent(this, menuSettings::class.java))
        }

        // Botón CONFIRMAR MENÚ
        findViewById<Button>(R.id.btnStartGame2).setOnClickListener {
            viewModel.confirmarSeleccion()
        }

        configurarObservadores()

        // Inicializar el juego en el ViewModel
        viewModel.iniciarJuego(avatar, comida)
    }

    private fun configurarObservadores() {
        // Observa la lista de alimentos y construye las tarjetas
        viewModel.alimentos.observe(this) { lista ->
            cargarTarjetas(lista)
        }

        // Observa el contenido de la bandeja seleccionada
        viewModel.bandeja.observe(this) { seleccionados ->
            actualizarBandeja(seleccionados)
        }

        // Observa el temporizador
        viewModel.tiempoRestanteMs.observe(this) { millis ->
            val segundosRestantes = (millis / 1000).toInt()
            if (millis <= 0) {
                tvTimer.text = "Se acabó el tiempo"
            } else {
                tvTimer.text = "⏱ ${segundosRestantes}s"
            }

            if (segundosRestantes <= 10 && millis > 0) {
                tvTimer.setTextColor(ContextCompat.getColor(this, R.color.color_error))
            } else {
                tvTimer.setTextColor(ContextCompat.getColor(this, R.color.color_text_primary))
            }
        }

        // Observa eventos únicos emitidos por el ViewModel
        viewModel.eventoUi.observe(this) { evento ->
            evento.getContentIfNotHandled()?.let { uiEvent ->
                when (uiEvent) {
                    is JuegoViewModel.UiEvent.PlayBubblySound -> SoundManager.playBubbly(this)
                    is JuegoViewModel.UiEvent.PlayUrgencySound -> SoundManager.playUrgency(this)
                    is JuegoViewModel.UiEvent.PlayErrorSound -> SoundManager.playError(this)
                    is JuegoViewModel.UiEvent.ShowToast -> {
                        Toast.makeText(this, uiEvent.mensaje, Toast.LENGTH_SHORT).show()
                    }
                    is JuegoViewModel.UiEvent.FinalizarPartida -> {
                        val intent = Intent(this, ResultadoActivity::class.java).apply {
                            putExtra("EXTRA_RESULTADO", uiEvent.resultado)
                            putExtra("EXTRA_AVATAR", uiEvent.resultado.avatar)
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    /** Infla una tarjeta (item_alimento.xml) por cada alimento en una grilla de 2 columnas. */
    private fun cargarTarjetas(alimentos: List<Alimento>) {
        gridAlimentos.removeAllViews()
        tarjetas.clear()

        val inflater = LayoutInflater.from(this)
        val margen = (8 * resources.displayMetrics.density).toInt()
        val margenInferior = (20 * resources.displayMetrics.density).toInt()

        alimentos.forEachIndexed { index, alimento ->
            val tarjeta = inflater.inflate(R.layout.item_alimento, gridAlimentos, false)
            tarjeta.findViewById<ImageView>(R.id.imgFood)
                .setImageResource(AlimentoVisual.imagenRes(this, alimento))
            tarjeta.findViewById<TextView>(R.id.tvFood).text = AlimentoVisual.nombreCorto(alimento)

            tarjeta.layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ).apply {
                width = 0
                setMargins(
                    if (index % 2 == 0) 0 else margen, 0,
                    if (index % 2 == 0) margen else 0, margenInferior
                )
            }

            tarjeta.setOnClickListener { viewModel.alternarAlimento(alimento) }
            tarjetas[alimento] = tarjeta
            gridAlimentos.addView(tarjeta)
        }
    }

    private fun actualizarBandeja(seleccionados: List<Alimento>) {
        tvBandeja.text = "Bandeja ${seleccionados.size}/${ReglasJuego.MAX_ALIMENTOS}"
        tarjetas.forEach { (alimento, tarjeta) ->
            val visible = if (seleccionados.contains(alimento)) View.VISIBLE else View.GONE
            tarjeta.findViewById<View>(R.id.viewSelected).visibility = visible
            tarjeta.findViewById<View>(R.id.tvCheck).visibility = visible
        }
    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.updateActivityColors(this)
        if (currentTheme != ThemeUtils.getSelectedTheme(this)) {
            recreate()
            return
        }
        viewModel.reanudarTimer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausarTimer()
    }
}
