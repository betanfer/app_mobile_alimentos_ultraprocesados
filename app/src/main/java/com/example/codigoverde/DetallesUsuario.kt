package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallesUsuario : AppCompatActivity() {

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT

    private lateinit var tvNombre: TextView
    private lateinit var tvEdad: TextView
    private lateinit var tvSexo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_usuario)
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

        tvNombre = findViewById(R.id.textView)
        tvEdad = findViewById(R.id.textView2)
        tvSexo = findViewById(R.id.textView3)

        // Botón Volver (<)
        val btnBack = findViewById<ImageButton>(R.id.btnBack4)
        btnBack?.setOnClickListener {
            SoundManager.playBubbly(this)
            finish()
        }

        // Botón Configuración
        val btnSettings = findViewById<View>(R.id.btnSettings)
        btnSettings?.setOnClickListener {
            SoundManager.playBubbly(this)
            startActivity(Intent(this, menuSettings::class.java))
        }

        // Botón Editar datos -> Abre Paso1AvatarActivity con los datos precargados para editar
        val btnEditar = findViewById<Button>(R.id.button)
        btnEditar?.setOnClickListener {
            SoundManager.playBubbly(this)
            val intent = Intent(this, Paso1AvatarActivity::class.java).apply {
                putExtra("MODO_EDICION", true)
            }
            startActivity(intent)
        }

        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val avatar = UserPreferences.obtenerAvatar(this)
        if (avatar != null) {
            tvNombre.text = avatar.nombre
            tvEdad.text = "Edad: ${avatar.edad} años"
            tvSexo.text = "Sexo: ${avatar.sexo}"
        } else {
            tvNombre.text = "Sin usuario registrado"
            tvEdad.text = "Edad: --"
            tvSexo.text = "Sexo: --"
        }
    }

    override fun onResume() {
        super.onResume()
        ThemeUtils.updateActivityColors(this)
        if (currentTheme != ThemeUtils.getSelectedTheme(this)) {
            recreate()
        }
        // Recarga los datos por si fueron modificados en Paso1AvatarActivity
        cargarDatosUsuario()
    }
}
