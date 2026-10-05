package com.example.codigoverde

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool

object SoundManager {
    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<Int, Int>()

    fun init(context: Context) {
        if (soundPool != null) return

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(8)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool?.let { sp ->
            soundMap[R.raw.sfx_btn_bubbly] = sp.load(context, R.raw.sfx_btn_bubbly, 1)
            soundMap[R.raw.sfx_btn_play] = sp.load(context, R.raw.sfx_btn_play, 1)
            soundMap[R.raw.sfx_game_finish] = sp.load(context, R.raw.sfx_game_finish, 1)
            soundMap[R.raw.sfx_result_positive] = sp.load(context, R.raw.sfx_result_positive, 1)
            soundMap[R.raw.sfx_result_negative] = sp.load(context, R.raw.sfx_result_negative, 1)
            soundMap[R.raw.sfx_urgency_timer] = sp.load(context, R.raw.sfx_urgency_timer, 1)
            soundMap[R.raw.sfx_error] = sp.load(context, R.raw.sfx_error, 1)
        }
    }

    fun isSoundEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getBoolean("sound_enabled", true)
    }

    fun setSoundEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
    }

    private fun playSound(context: Context, resId: Int) {
        if (!isSoundEnabled(context)) return

        if (soundPool == null) {
            init(context.applicationContext)
        }
        val soundId = soundMap[resId] ?: return
        soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
    }

    // 1. Botones principales (bubbly pop)
    fun playBubbly(context: Context) = playSound(context, R.raw.sfx_btn_bubbly)

    // 2. Botón "Jugar" (emocionante)
    fun playStartGame(context: Context) = playSound(context, R.raw.sfx_btn_play)

    // 3. Fin de la partida
    fun playGameFinish(context: Context) = playSound(context, R.raw.sfx_game_finish)

    // 4. Resultados positivos (éxito)
    fun playResultPositive(context: Context) = playSound(context, R.raw.sfx_result_positive)

    // 5. Resultados negativos (intentar de nuevo)
    fun playResultNegative(context: Context) = playSound(context, R.raw.sfx_result_negative)

    // 6. Alarma de prisa cuando quedan 5 segundos o menos
    fun playUrgency(context: Context) = playSound(context, R.raw.sfx_urgency_timer)

    // 7. Error al intentar superar el límite de 3 alimentos
    fun playError(context: Context) = playSound(context, R.raw.sfx_error)

    fun release() {
        soundPool?.release()
        soundPool = null
        soundMap.clear()
    }
}
