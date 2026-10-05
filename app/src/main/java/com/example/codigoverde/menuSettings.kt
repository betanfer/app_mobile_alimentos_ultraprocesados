package com.example.codigoverde

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.switchmaterial.SwitchMaterial

class menuSettings : AppCompatActivity() {

    private lateinit var switchSound: SwitchMaterial
    private lateinit var switchProtanopia: SwitchMaterial
    private lateinit var switchTritanopia: SwitchMaterial

    private var isUpdatingSwitches = false

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_settings)
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

        // Cruz (X) -> Vuelve a la pantalla en la que estabas antes
        val btnClose = findViewById<ImageButton>(R.id.btnBack3)
        btnClose?.setOnClickListener {
            finish()
        }

        switchSound = findViewById(R.id.switch1)
        switchProtanopia = findViewById(R.id.switch2)
        switchTritanopia = findViewById(R.id.switch3)

        loadSettings()
        setupListeners()
    }

    private fun loadSettings() {
        isUpdatingSwitches = true

        // 1. Estado del Sonido
        switchSound.isChecked = SoundManager.isSoundEnabled(this)

        // 2. Estado de Paleta de Colores
        val currentTheme = ThemeUtils.getSelectedTheme(this)
        switchProtanopia.isChecked = (currentTheme == ThemeUtils.THEME_PROTANOPIA)
        switchTritanopia.isChecked = (currentTheme == ThemeUtils.THEME_TRITANOPIA)

        isUpdatingSwitches = false
    }

    private fun setupListeners() {
        // Switch 1: Sonido
        switchSound.setOnCheckedChangeListener { _, isChecked ->
            if (isUpdatingSwitches) return@setOnCheckedChangeListener
            SoundManager.setSoundEnabled(this, isChecked)
        }

        // Switch 2: Protanopia / Deuteranopia
        switchProtanopia.setOnCheckedChangeListener { _, isChecked ->
            if (isUpdatingSwitches) return@setOnCheckedChangeListener
            if (isChecked) {
                isUpdatingSwitches = true
                switchTritanopia.isChecked = false
                isUpdatingSwitches = false
                ThemeUtils.setSelectedTheme(this, ThemeUtils.THEME_PROTANOPIA)
            } else {
                if (!switchTritanopia.isChecked) {
                    ThemeUtils.setSelectedTheme(this, ThemeUtils.THEME_DEFAULT)
                }
            }
            recreate()
        }

        // Switch 3: Tritanopia
        switchTritanopia.setOnCheckedChangeListener { _, isChecked ->
            if (isUpdatingSwitches) return@setOnCheckedChangeListener
            if (isChecked) {
                isUpdatingSwitches = true
                switchProtanopia.isChecked = false
                isUpdatingSwitches = false
                ThemeUtils.setSelectedTheme(this, ThemeUtils.THEME_TRITANOPIA)
            } else {
                if (!switchProtanopia.isChecked) {
                    ThemeUtils.setSelectedTheme(this, ThemeUtils.THEME_DEFAULT)
                }
            }
            recreate()
        }
    }
}
