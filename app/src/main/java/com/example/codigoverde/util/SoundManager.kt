package com.example.codigoverde.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import com.example.codigoverde.R

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
            .setMaxStreams(4)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool?.let { pool ->
            soundMap[R.raw.sfx_btn_bubbly] = pool.load(context, R.raw.sfx_btn_bubbly, 1)
            soundMap[R.raw.sfx_btn_play] = pool.load(context, R.raw.sfx_btn_play, 1)
            soundMap[R.raw.sfx_game_finish] = pool.load(context, R.raw.sfx_game_finish, 1)
            soundMap[R.raw.sfx_result_positive] = pool.load(context, R.raw.sfx_result_positive, 1)
            soundMap[R.raw.sfx_result_negative] = pool.load(context, R.raw.sfx_result_negative, 1)
            soundMap[R.raw.sfx_urgency_timer] = pool.load(context, R.raw.sfx_urgency_timer, 1)
            soundMap[R.raw.sfx_error] = pool.load(context, R.raw.sfx_error, 1)
        }
    }

    fun isSoundEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences("CodigoVerdePrefs", AppCompatActivity.MODE_PRIVATE)
        return prefs.getBoolean("sound_enabled", true)
    }

    fun setSoundEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences("CodigoVerdePrefs", AppCompatActivity.MODE_PRIVATE)
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
    }

    private fun playSound(context: Context, resId: Int) {
        if (!isSoundEnabled(context)) return

        if (soundPool == null) {
            init(context.applicationContext)
        }

        val soundId = soundMap[resId]
        if (soundId != null && soundId != 0) {
            soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun playBubbly(context: Context) = playSound(context, R.raw.sfx_btn_bubbly)
    fun playStartGame(context: Context) = playSound(context, R.raw.sfx_btn_play)
    fun playGameFinish(context: Context) = playSound(context, R.raw.sfx_game_finish)
    fun playResultPositive(context: Context) = playSound(context, R.raw.sfx_result_positive)
    fun playResultNegative(context: Context) = playSound(context, R.raw.sfx_result_negative)
    fun playUrgency(context: Context) = playSound(context, R.raw.sfx_urgency_timer)
    fun playError(context: Context) = playSound(context, R.raw.sfx_error)

    fun release() {
        soundPool?.release()
        soundPool = null
        soundMap.clear()
    }
}
