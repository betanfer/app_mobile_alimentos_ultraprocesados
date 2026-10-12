package com.example.codigoverde.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.R
import com.example.codigoverde.model.Avatar
import com.example.codigoverde.util.SoundManager
import com.example.codigoverde.util.ThemeUtils
import com.example.codigoverde.viewmodel.AvatarViewModel

/**
 * Pantalla de Creación y Edición del Avatar (View en MVVM):
 * Delega la carga y validación de datos en AvatarViewModel.
 */
class Paso1AvatarActivity : AppCompatActivity() {

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT
    private val viewModel: AvatarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso1_avatar)
        ThemeUtils.updateActivityColors(this)

        val etNombre = findViewById<EditText>(R.id.etNombreAvatar)
        val rgSexo = findViewById<RadioGroup>(R.id.rgGenero)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        val etEdad = findViewById<EditText>(R.id.etEdad)

        val rbHombre = findViewById<android.widget.RadioButton>(R.id.rbHombre)
        val rbMujer = findViewById<android.widget.RadioButton>(R.id.rbMujer)

        fun actualizarEstilosGenero(idSeleccionado: Int) {
            rbHombre.setBackgroundResource(if (idSeleccionado == R.id.rbHombre) R.drawable.bg_card_selected else R.drawable.bg_card_unselected)
            rbMujer.setBackgroundResource(if (idSeleccionado == R.id.rbMujer) R.drawable.bg_card_selected else R.drawable.bg_card_unselected)
        }

        rgSexo.setOnCheckedChangeListener { _, checkedId ->
            actualizarEstilosGenero(checkedId)
        }
        actualizarEstilosGenero(rgSexo.checkedRadioButtonId)

        val esModoEdicion = intent.getBooleanExtra("MODO_EDICION", false)
        if (esModoEdicion) {
            btnContinuar.text = "Guardar Cambios"
        }

        // Precarga de datos observando el ViewModel
        viewModel.avatarActual.observe(this) { avatarGuardado ->
            if (avatarGuardado != null) {
                if (etNombre.text.isEmpty()) etNombre.setText(avatarGuardado.nombre)
                if (avatarGuardado.edad > 0 && etEdad.text.isEmpty()) etEdad.setText(avatarGuardado.edad.toString())
                if (avatarGuardado.sexo == Avatar.SEXO_FEMENINO) {
                    rgSexo.check(R.id.rbMujer)
                } else {
                    rgSexo.check(R.id.rbHombre)
                }
            }
        }

        // Observador de eventos únicos (Toasts, navegación)
        viewModel.eventoUi.observe(this) { evento ->
            evento.getContentIfNotHandled()?.let { uiEvent ->
                when (uiEvent) {
                    is AvatarViewModel.UiEvent.PlayBubblySound -> SoundManager.playBubbly(this)
                    is AvatarViewModel.UiEvent.ShowToast -> {
                        if (uiEvent.resId != null) {
                            Toast.makeText(this, uiEvent.resId, Toast.LENGTH_SHORT).show()
                        } else if (uiEvent.mensaje != null) {
                            Toast.makeText(this, uiEvent.mensaje, Toast.LENGTH_SHORT).show()
                        }
                    }
                    is AvatarViewModel.UiEvent.GuardadoExitoso -> {
                        if (uiEvent.esModoEdicion) {
                            Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val intent = Intent(this, SeleccionarMenuActivity::class.java).apply {
                                putExtra("EXTRA_AVATAR", uiEvent.avatar)
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }

        btnContinuar.setOnClickListener {
            val esMasculino = rgSexo.checkedRadioButtonId == R.id.rbHombre
            viewModel.guardarPerfil(
                nombre = etNombre.text.toString(),
                edadTexto = etEdad.text.toString(),
                esMasculino = esMasculino,
                esModoEdicion = esModoEdicion
            )
        }

        findViewById<View>(R.id.btnSettings)?.setOnClickListener {
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