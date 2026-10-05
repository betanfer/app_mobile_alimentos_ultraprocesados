package com.example.codigoverde

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Pantalla de juego: tarjetas con imagen de cada alimento y la lógica de la partida:
 *  - Carga los alimentos de la comida elegida desde assets/menu_alimentos_puntuado.txt
 *  - El jugador elige hasta 3 alimentos en 30 segundos
 *  - Al confirmar (o al terminar el tiempo) se suman los puntajes al avatar y se muestran los resultados
 */
class JuegoActivity : AppCompatActivity() {

    companion object {
        private const val MAX_ALIMENTOS = 3
        private const val TIEMPO_TOTAL_MS = 30000L
    }

    // Referencia al temporizador para poder cancelarlo y evitar fugas de memoria
    private var timer: CountDownTimer? = null

    // Tiempo restante del timer en milisegundos (se conserva si la pantalla se pausa)
    private var tiempoRestanteMs: Long = TIEMPO_TOTAL_MS

    private lateinit var avatar: Avatar
    private lateinit var alimentos: List<Alimento>
    private val bandeja = mutableListOf<Alimento>()
    private val tarjetas = mutableMapOf<Alimento, View>()

    private lateinit var tvTimer: TextView
    private lateinit var tvBandeja: TextView

    // Evita procesar dos veces (botón + fin del timer)
    private var partidaTerminada = false

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

        avatar = intent.getSerializableExtra("EXTRA_AVATAR") as Avatar
        val comida = intent.getStringExtra("COMIDA_SELECCIONADA") ?: "Desayuno"

        tvTimer = findViewById(R.id.tvTimer)
        tvBandeja = findViewById(R.id.tvBandeja)
        findViewById<TextView>(R.id.tvGameTitle).text = "OPCIONES DE ${comida.uppercase()}"

        // Botón Cruz (X) -> Regresa a la primera pantalla (MainActivity)
        val btnClose = findViewById<ImageButton>(R.id.btnBack2)
        btnClose?.setOnClickListener {
            SoundManager.playBubbly(this)
            partidaTerminada = true
            timer?.cancel()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        // Botón CONFIRMAR MENÚ -> procesa la bandeja
        findViewById<Button>(R.id.btnStartGame2).setOnClickListener {
            if (bandeja.isEmpty()) {
                Toast.makeText(this, "Elegí al menos un alimento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            procesarFinal()
        }

        alimentos = AlimentoRepository.cargarAlimentos(this).filter {
            it.comida.equals(comida, ignoreCase = true)
        }
        cargarTarjetas(findViewById(R.id.gridAlimentos))
        actualizarBandeja()
    }

    /** Infla una tarjeta (item_alimento.xml) por cada alimento en una grilla de 2 columnas. */
    private fun cargarTarjetas(grid: GridLayout) {
        val inflater = LayoutInflater.from(this)
        val margen = (8 * resources.displayMetrics.density).toInt()
        val margenInferior = (20 * resources.displayMetrics.density).toInt()

        alimentos.forEachIndexed { index, alimento ->
            val tarjeta = inflater.inflate(R.layout.item_alimento, grid, false)
            tarjeta.findViewById<ImageView>(R.id.imgFood)
                .setImageResource(AlimentoVisual.imagenRes(this, alimento))
            tarjeta.findViewById<TextView>(R.id.tvFood).text = AlimentoVisual.nombreCorto(alimento)

            tarjeta.layoutParams = GridLayout.LayoutParams(
                GridLayout.spec(GridLayout.UNDEFINED),
                GridLayout.spec(GridLayout.UNDEFINED, 1f)
            ).apply {
                width = 0
                setMargins(
                    if (index % 2 == 0) 0 else margen, 0,
                    if (index % 2 == 0) margen else 0, margenInferior
                )
            }

            tarjeta.setOnClickListener { alternarAlimento(alimento) }
            tarjetas[alimento] = tarjeta
            grid.addView(tarjeta)
        }
    }

    /** Agrega o quita un alimento de la bandeja (máximo 3). */
    private fun alternarAlimento(alimento: Alimento) {
        if (partidaTerminada) return

        if (bandeja.contains(alimento)) {
            bandeja.remove(alimento)
            SoundManager.playBubbly(this)
        } else if (bandeja.size < MAX_ALIMENTOS) {
            bandeja.add(alimento)
            SoundManager.playBubbly(this)
        } else {
            SoundManager.playError(this)
            Toast.makeText(this, "Máximo $MAX_ALIMENTOS alimentos", Toast.LENGTH_SHORT).show()
            return
        }
        actualizarBandeja()
    }

    private fun actualizarBandeja() {
        tvBandeja.text = "Bandeja ${bandeja.size}/$MAX_ALIMENTOS"
        tarjetas.forEach { (alimento, tarjeta) ->
            val visible = if (bandeja.contains(alimento)) View.VISIBLE else View.GONE
            tarjeta.findViewById<View>(R.id.viewSelected).visibility = visible
            tarjeta.findViewById<View>(R.id.tvCheck).visibility = visible
        }
    }

    // La pantalla pasa a primer plano y se vuelve interactiva
    override fun onResume() {
        super.onResume()
        if (partidaTerminada) return

        // Se crea e inicia el temporizador (continúa desde el tiempo restante)
        timer = object : CountDownTimer(tiempoRestanteMs, 1000) {
            // Se ejecuta cada segundo (1000 ms)
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestanteMs = millisUntilFinished
                val segundosRestantes = (millisUntilFinished / 1000).toInt()
                tvTimer.text = "⏱ ${segundosRestantes}s"
                // Últimos 10 segundos en rojo
                if (segundosRestantes <= 10) {
                    tvTimer.setTextColor(ContextCompat.getColor(this@JuegoActivity, R.color.color_error))
                }
                // Sonido de prisa cuando queden 5 segundos o menos
                if (segundosRestantes in 1..5) {
                    SoundManager.playUrgency(this@JuegoActivity)
                }
            }

            // Se ejecuta cuando el temporizador llega a cero
            override fun onFinish() {
                tvTimer.text = "Se acabó el tiempo"
                procesarFinal()
            }
        }.start()
    }

    // La pantalla deja de estar visible: se pausa el timer
    override fun onPause() {
        super.onPause()
        timer?.cancel()
    }

    /** Suma los atributos de los alimentos elegidos al avatar y abre la pantalla de resultados. */
    private fun procesarFinal() {
        if (partidaTerminada) return
        partidaTerminada = true
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

    // Se destruye la pantalla para liberar memoria
    override fun onDestroy() {
        super.onDestroy()

        // Detiene el timer para liberar memoria
        timer?.cancel()
        timer = null
    }

}
