package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Paso1AvatarActivity : AppCompatActivity() {

    private var currentTheme: String = ThemeUtils.THEME_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        currentTheme = ThemeUtils.applyTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso1_avatar)
        ThemeUtils.updateActivityColors(this)

        val etNombre = findViewById<EditText>(R.id.etNombreAvatar)
        val rgSexo = findViewById<RadioGroup>(R.id.rgGenero)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)

        val esModoEdicion = intent.getBooleanExtra("MODO_EDICION", false)

        val etEdad = findViewById<EditText>(R.id.etEdad)

        // Precarga de datos
        val avatarGuardado = UserPreferences.obtenerAvatar(this)
        if (avatarGuardado != null) {
            etNombre.setText(avatarGuardado.nombre)
            if (avatarGuardado.edad > 0) etEdad.setText(avatarGuardado.edad.toString())
            if (avatarGuardado.sexo == Avatar.SEXO_FEMENINO) {
                rgSexo.check(R.id.rbMujer)
            } else {
                rgSexo.check(R.id.rbHombre)
            }
        }

        if (esModoEdicion) {
            btnContinuar.text = "Guardar Cambios"
        }

        btnContinuar.setOnClickListener {
            SoundManager.playBubbly(this)
            val nombre = etNombre.text.toString().trim()
            val sexoId = rgSexo.checkedRadioButtonId
            val edad = etEdad.text.toString().trim().toIntOrNull()

            if (nombre.isEmpty() || sexoId == -1 || edad == null || edad !in 1..120) {
                Toast.makeText(this, R.string.error_datos_avatar, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sexo = if (sexoId == R.id.rbHombre) Avatar.SEXO_MASCULINO else Avatar.SEXO_FEMENINO
            val avatar = Avatar(nombre, sexo, edad)

            // Guardar permanentemente
            UserPreferences.guardarAvatar(this, avatar)

            if (esModoEdicion) {
                Toast.makeText(this, "Datos actualizados correctamente", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                val intent = Intent(this, SeleccionarMenuActivity::class.java).apply {
                    putExtra("EXTRA_AVATAR", avatar)
                }
                startActivity(intent)
                finish()
            }
        }

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