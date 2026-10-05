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

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val rgSexo = findViewById<RadioGroup>(R.id.rgSexo)
        val btnComenzar = findViewById<Button>(R.id.btnComenzar)

        val esModoEdicion = intent.getBooleanExtra("MODO_EDICION", false)

        // Precarga los datos guardados si existen
        val avatarGuardado = UserPreferences.obtenerAvatar(this)
        if (avatarGuardado != null) {
            etNombre.setText(avatarGuardado.nombre)
            etEdad.setText(avatarGuardado.edad.toString())
            if (avatarGuardado.sexo.equals("Femenino", ignoreCase = true)) {
                rgSexo.check(R.id.rbFemenino)
            } else {
                rgSexo.check(R.id.rbMasculino)
            }
        }

        if (esModoEdicion) {
            btnComenzar.text = "Guardar Cambios"
        }

        btnComenzar.setOnClickListener {
            SoundManager.playBubbly(this)
            val nombre = etNombre.text.toString().trim()
            val edadStr = etEdad.text.toString().trim()
            val sexoId = rgSexo.checkedRadioButtonId

            if (nombre.isEmpty() || edadStr.isEmpty() || sexoId == -1) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sexo = if (sexoId == R.id.rbMasculino) "Masculino" else "Femenino"
            val avatar = Avatar(nombre, sexo, edadStr.toInt())

            // Guarda persistentemente en SharedPreferences
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
