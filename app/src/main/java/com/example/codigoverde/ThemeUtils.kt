package com.example.codigoverde

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

object ThemeUtils {
    const val THEME_DEFAULT = "default"
    const val THEME_PROTANOPIA = "protanopia"
    const val THEME_TRITANOPIA = "tritanopia"

    fun getSelectedTheme(context: Context): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getString("theme_mode", THEME_DEFAULT) ?: THEME_DEFAULT
    }

    fun setSelectedTheme(context: Context, themeMode: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("theme_mode", themeMode).apply()
    }

    fun applyTheme(activity: AppCompatActivity): String {
        val themeMode = getSelectedTheme(activity)
        when (themeMode) {
            THEME_PROTANOPIA -> activity.setTheme(R.style.Theme_CodigoVerde_Protanopia)
            THEME_TRITANOPIA -> activity.setTheme(R.style.Theme_CodigoVerde_Tritanopia)
            else -> activity.setTheme(R.style.Theme_CodigoVerde)
        }
        return themeMode
    }

    fun updateActivityColors(activity: AppCompatActivity) {
        val themeMode = getSelectedTheme(activity)

        val backgroundColor = when (themeMode) {
            THEME_PROTANOPIA -> Color.parseColor("#1F1F29")
            THEME_TRITANOPIA -> Color.parseColor("#192929")
            else -> Color.parseColor("#172929")
        }

        val primaryColor = when (themeMode) {
            THEME_PROTANOPIA -> Color.parseColor("#B7B79B")
            THEME_TRITANOPIA -> Color.parseColor("#A7A8AC")
            else -> Color.parseColor("#A4CE8B")
        }

        val primaryTextColor = when (themeMode) {
            THEME_PROTANOPIA -> Color.parseColor("#1F1F29")
            THEME_TRITANOPIA -> Color.parseColor("#192929")
            else -> Color.parseColor("#172929")
        }

        activity.findViewById<View>(R.id.main)?.setBackgroundColor(backgroundColor)

        val buttonIds = intArrayOf(
            R.id.btnConfirm,
            R.id.btnStartGame,
            R.id.btnStartGame2,
            R.id.btnComenzar
        )

        for (id in buttonIds) {
            activity.findViewById<View>(id)?.let { view ->
                if (view is MaterialButton) {
                    view.backgroundTintList = ColorStateList.valueOf(primaryColor)
                    view.setTextColor(primaryTextColor)
                } else if (view is Button) {
                    view.backgroundTintList = ColorStateList.valueOf(primaryColor)
                    view.setTextColor(primaryTextColor)
                }
            }
        }
    }
}
