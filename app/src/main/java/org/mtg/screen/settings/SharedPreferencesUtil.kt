package org.mtg.screen.settings

import android.content.Context
import org.mtg.app.MagicLeagueApp

object SharedPreferencesUtil {
    private const val PREFERENCES_FILE = "org.mtg.screen.settings.preferences"
    private const val DARK_MODE_KEY = "DARK_MODE_KEY"

    fun saveDarkMode(value: Boolean) {
        MagicLeagueApp.applicationContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit()
            .putBoolean(DARK_MODE_KEY, value).apply()
    }

    fun darkMode() =
        MagicLeagueApp.applicationContext().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
            .getBoolean(DARK_MODE_KEY, false)
}
