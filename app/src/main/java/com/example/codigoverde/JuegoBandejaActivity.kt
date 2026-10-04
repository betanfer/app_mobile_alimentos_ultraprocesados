package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.Alimento
import com.example.codigoverde.AlimentoRepository
import com.example.codigoverde.Avatar

class JuegoBandejaActivity : AppCompatActivity() {

    private lateinit var avatar: Avatar
    private val bandeja = mutableListOf<Alimento>()
    private var timer: CountDownTimer? = null
    private lateinit var tvTimer: TextView
    private lateinit var tvBandeja: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_juego_bandeja)

        avatar = intent.getSerializableExtra("EXTRA_AVATAR") as Avatar
        val comida = intent.getStringExtra("COMIDA_SELECCIONADA") ?: "Desayuno"

        tvTimer = findViewById(R.id.tvTimer)
        tvBandeja = findViewById(R.id.tvBandeja)
        val listView = findViewById<ListView>(R.id.listViewAlimentos)

        val alimentos = AlimentoRepository.cargarAlimentos(this).filter {
            it.comida.equals(comida, ignoreCase = true)
        }

        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alimentos.map { it.opcion })

        listView.setOnItemClickListener { _, _, pos, _ ->
            if (bandeja.size < 3) {
                bandeja.add(alimentos[pos])
                tvBandeja.text = "Bandeja (${bandeja.size}/3): " + bandeja.joinToString(", ") { it.opcion }
            } else {
                Toast.makeText(this, "Máximo 3 alimentos", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnProcesar).setOnClickListener {
            procesarFinal()
        }

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millis: Long) {
                tvTimer.text = "Tiempo: ${millis / 1000}s"
            }
            override fun onFinish() {
                procesarFinal()
            }
        }.start()
    }

    private fun procesarFinal() {
        timer?.cancel()

        for (item in bandeja) {
            avatar.salud += item.nivelSaludable
            avatar.saciedad += item.saciedad
            avatar.vitalidad += item.vitalidad
        }

        val intent = Intent(this, ResultadoActivity::class.java).apply {
            putExtra("EXTRA_AVATAR", avatar)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
