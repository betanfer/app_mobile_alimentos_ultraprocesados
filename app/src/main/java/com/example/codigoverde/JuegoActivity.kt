package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class JuegoActivity : AppCompatActivity() {

    // Referencia al temporizador para poder cancelarlo y evitar fugas de memoria
    private var timer: CountDownTimer? = null

    // Tiempo inicial del timer en milisegundos
    private var tiempoRestanteMs: Long = 30000

    // Componente gráfico donde se muestra el temporizador (no definido aun en XML)
    private lateinit var tvTimer: TextView

    // Se crea la pantalla
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_juego)

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

        // Botón Cruz (X) -> Regresa a la primera pantalla (MainActivity)
        val btnClose = findViewById<ImageButton>(R.id.btnBack2)
        btnClose?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Vincula el temporizador con el componente gráfico
        // tvTimer = findViewById(R.id.tvTimer)
    }

    // La pantalla pasa a primer plano y se vuelve interactiva
    override fun onResume() {
        super.onResume()

        // Se crea e inicia el temporizador
        timer = object : CountDownTimer(tiempoRestanteMs, 1000) {
            // Se ejecuta cada segundo (1000 ms)
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestanteMs = millisUntilFinished
                val segundosRestantes = (millisUntilFinished / 1000).toInt()
                tvTimer.text = "Quedan $segundosRestantes segundos"
            }

            // Se ejecuta cuando el temporizador llega a cero
            override fun onFinish() {
                tvTimer.text = "Se acabó el tiempo"
            }
        }.start()
    }

    // Se destruye la pantalla para liberar memoria
    override fun onDestroy() {
        super.onDestroy()

        // Detiene el timer para liberar memoria
        timer?.cancel()
        timer = null
    }

}
