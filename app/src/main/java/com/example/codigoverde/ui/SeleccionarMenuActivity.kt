package com.example.codigoverde.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.codigoverde.R
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.model.Comida
import com.example.codigoverde.util.SoundManager
import com.example.codigoverde.util.ThemeUtils
import com.example.codigoverde.viewmodel.SeleccionMenuViewModel

/**
 * Selección del modo de juego (View en MVVM):
 * Observa SeleccionMenuViewModel para reflejar el modo (automático o personalizado)
 * y la comida seleccionada.
 */
class SeleccionarMenuActivity : AppCompatActivity() {

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT
    private val viewModel: SeleccionMenuViewModel by viewModels()

    private lateinit var avatar: Avatar
    private lateinit var cardAutomatic: View
    private lateinit var cardCustom: View
    private lateinit var tvHorarioActual: TextView
    private lateinit var chips: Map<Comida, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccionarmenuactivity)
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

        avatar = intent.getSerializableExtra("EXTRA_AVATAR") as Avatar

        cardAutomatic = findViewById(R.id.cardAutomatic)
        cardCustom = findViewById(R.id.cardCustom)
        tvHorarioActual = findViewById(R.id.tvHorarioActual)

        chips = mapOf(
            Comida.DESAYUNO to findViewById(R.id.chipDesayuno),
            Comida.ALMUERZO to findViewById(R.id.chipAlmuerzo),
            Comida.MERIENDA to findViewById(R.id.chipMerienda),
            Comida.CENA to findViewById(R.id.chipCena)
        )

        // Clics delegados al ViewModel
        cardAutomatic.setOnClickListener {
            viewModel.seleccionarModoAutomatico()
        }

        cardCustom.setOnClickListener {
            viewModel.seleccionarModoManual()
        }

        chips.forEach { (comida, chip) ->
            chip.setOnClickListener {
                viewModel.seleccionarComida(comida)
            }
        }

        findViewById<Button>(R.id.btnConfirm)?.setOnClickListener {
            viewModel.confirmarSeleccion()
        }

        findViewById<View>(R.id.btnSettings)?.setOnClickListener {
            SoundManager.playBubbly(this)
            startActivity(Intent(this, menuSettings::class.java))
        }

        configurarObservadores()
    }

    private fun configurarObservadores() {
        viewModel.comidaHorarioActual.observe(this) { comida ->
            tvHorarioActual.text = "ACTUAL: ${comida.titulo.uppercase()}"
        }

        viewModel.modoAutomatico.observe(this) {
            actualizarEstilosVisuales()
        }

        viewModel.comidaElegida.observe(this) {
            actualizarEstilosVisuales()
        }

        viewModel.eventoUi.observe(this) { evento ->
            evento.getContentIfNotHandled()?.let { uiEvent ->
                when (uiEvent) {
                    is SeleccionMenuViewModel.UiEvent.PlayBubblySound -> SoundManager.playBubbly(this)
                    is SeleccionMenuViewModel.UiEvent.ShowToast -> {
                        Toast.makeText(this, uiEvent.mensaje, Toast.LENGTH_SHORT).show()
                    }
                    is SeleccionMenuViewModel.UiEvent.IniciarJuego -> {
                        val intent = Intent(this, JuegoActivity::class.java).apply {
                            putExtra("EXTRA_AVATAR", avatar)
                            putExtra("COMIDA_SELECCIONADA", uiEvent.comidaTitulo)
                        }
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun actualizarEstilosVisuales() {
        val automatico = viewModel.modoAutomatico.value == true
        val comidaSeleccionada = viewModel.comidaElegida.value

        cardAutomatic.setBackgroundResource(
            if (automatico) R.drawable.bg_card_selected else R.drawable.bg_card_unselected
        )
        cardCustom.setBackgroundResource(
            if (!automatico) R.drawable.bg_card_selected else R.drawable.bg_card_unselected
        )

        chips.forEach { (comida, chip) ->
            val seleccionado = !automatico && comida == comidaSeleccionada
            chip.setBackgroundResource(if (seleccionado) R.drawable.bg_tag_selected else R.drawable.bg_tag)
            chip.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (seleccionado) R.color.color_button_primary_text else R.color.color_text_secondary
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.updateActivityColors(this)
        if (currentTheme != ThemeUtils.getSelectedTheme(this)) {
            recreate()
            return
        }
        viewModel.actualizarHorarioActual()
    }
}
