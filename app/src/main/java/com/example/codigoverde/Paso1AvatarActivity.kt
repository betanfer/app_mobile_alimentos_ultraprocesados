package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Paso1AvatarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paso1_avatar)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEdad = findViewById<EditText>(R.id.etEdad)
        val rgSexo = findViewById<RadioGroup>(R.id.rgSexo)
        val btnComenzar = findViewById<Button>(R.id.btnComenzar)

        btnComenzar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val edadStr = etEdad.text.toString().trim()
            val sexoId = rgSexo.checkedRadioButtonId

            if (nombre.isEmpty() || edadStr.isEmpty() || sexoId == -1) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sexo = if (sexoId == R.id.rbMasculino) "Masculino" else "Femenino"
            val avatar = Avatar(nombre, sexo, edadStr.toInt())

            val intent = Intent(this, Paso2MenuActivity::class.java).apply {
                putExtra("EXTRA_AVATAR", avatar)
            }
            startActivity(intent)
        }
    }
}