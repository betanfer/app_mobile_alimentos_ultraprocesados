package com.example.codigoverde

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {
    private const val PREF_NAME = "CodigoVerdeUserPrefs"
    private const val KEY_NOMBRE = "user_nombre"
    private const val KEY_EDAD = "user_edad"
    private const val KEY_SEXO = "user_sexo"
    private const val KEY_HAS_AVATAR = "has_avatar"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    /** Guarda los datos del avatar en el almacenamiento local persistente (SharedPreferences). */
    fun guardarAvatar(context: Context, avatar: Avatar) {
        getPrefs(context).edit().apply {
            putString(KEY_NOMBRE, avatar.nombre)
            putInt(KEY_EDAD, avatar.edad)
            putString(KEY_SEXO, avatar.sexo)
            putBoolean(KEY_HAS_AVATAR, true)
            apply()
        }
    }

    /** Indica si ya hay datos de avatar guardados. */
    fun existeAvatar(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_HAS_AVATAR, false)
    }

    /** Recupera el avatar guardado o null si aún no se ha creado ninguno. */
    fun obtenerAvatar(context: Context): Avatar? {
        val prefs = getPrefs(context)
        if (!prefs.getBoolean(KEY_HAS_AVATAR, false)) return null

        val nombre = prefs.getString(KEY_NOMBRE, "") ?: ""
        val edad = prefs.getInt(KEY_EDAD, 0)
        val sexo = prefs.getString(KEY_SEXO, "Masculino") ?: "Masculino"

        return Avatar(
            nombre = nombre,
            sexo = sexo,
            edad = edad
        )
    }

    /** Borra los datos guardados. */
    fun borrarAvatar(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
