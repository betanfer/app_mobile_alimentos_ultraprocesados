package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

/**
 * Selección del modo de juego:
 *  - AUTOMÁTICO: la comida se elige según la hora actual.
 *  - YO ELIJO: el jugador elige Desayuno / Almuerzo / Merienda / Cena con los chips.
 */
class SeleccionarMenuActivity : AppCompatActivity() {

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT

    private lateinit var avatar: Avatar

    private var modoAutomatico = true
    private var comidaElegida: String? = null

    private lateinit var cardAutomatic: View
    private lateinit var cardCustom: View
    private lateinit var chips: Map<String, TextView>

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
        chips = mapOf(
            "Desayuno" to findViewById(R.id.chipDesayuno),
            "Almuerzo" to findViewById(R.id.chipAlmuerzo),
            "Merienda" to findViewById(R.id.chipMerienda),
            "Cena" to findViewById(R.id.chipCena)
        )

        // Muestra la comida que corresponde a la hora actual
        findViewById<TextView>(R.id.tvHorarioActual).text =
            "ACTUAL: ${obtenerComidaPorHoraActual().uppercase()}"

        // OPCIÓN 1: Automático
        cardAutomatic.setOnClickListener {
            SoundManager.playBubbly(this)
            modoAutomatico = true
            actualizarSeleccion()
        }

        // OPCIÓN 2: Yo elijo
        cardCustom.setOnClickListener {
            SoundManager.playBubbly(this)
            modoAutomatico = false
            actualizarSeleccion()
        }

        // Chips de comidas (al tocar uno se activa el modo "Yo elijo")
        chips.forEach { (comida, chip) ->
            chip.setOnClickListener {
                SoundManager.playBubbly(this)
                modoAutomatico = false
                comidaElegida = comida
                actualizarSeleccion()
            }
        }

        // Botón CONFIRMAR Y EMPEZAR -> Navega a JuegoActivity
        val btnConfirm = findViewById<Button>(R.id.btnConfirm)
        btnConfirm?.setOnClickListener {
            val comida = if (modoAutomatico) obtenerComidaPorHoraActual() else comidaElegida

            if (comida == null) {
                Toast.makeText(this, "Elegí una comida para continuar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            SoundManager.playBubbly(this)
            val intent = Intent(this, JuegoActivity::class.java).apply {
                putExtra("EXTRA_AVATAR", avatar)
                putExtra("COMIDA_SELECCIONADA", comida)
            }
            startActivity(intent)
        }

        val btnSettings = findViewById<View>(R.id.btnSettings)
        btnSettings?.setOnClickListener {
            SoundManager.playBubbly(this)
            startActivity(Intent(this, menuSettings::class.java))
        }

        actualizarSeleccion()
    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.updateActivityColors(this)
        if (currentTheme != ThemeUtils.getSelectedTheme(this)) {
            recreate()
        }
    }

    private fun actualizarSeleccion() {
        cardAutomatic.setBackgroundResource(
            if (modoAutomatico) R.drawable.bg_card_selected else R.drawable.bg_card_unselected
        )
        cardCustom.setBackgroundResource(
            if (!modoAutomatico) R.drawable.bg_card_selected else R.drawable.bg_card_unselected
        )

        chips.forEach { (comida, chip) ->
            val seleccionado = !modoAutomatico && comida == comidaElegida
            chip.setBackgroundResource(if (seleccionado) R.drawable.bg_tag_selected else R.drawable.bg_tag)
            chip.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (seleccionado) R.color.color_button_primary_text else R.color.color_text_secondary
                )
            )
        }
    }

    private fun obtenerComidaPorHoraActual(): String {
        val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hora) {
            in 6..10 -> "Desayuno"
            in 11..14 -> "Almuerzo"
            in 15..18 -> "Merienda"
            else -> "Cena"
        }
    }
}
