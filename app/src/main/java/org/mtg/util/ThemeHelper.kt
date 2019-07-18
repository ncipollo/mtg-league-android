package org.mtg.util

import org.mtg.R

object ThemeHelper {

    fun textColorPrimary(isDarkMode: Boolean) =
        if (isDarkMode) {
            R.color.textColorPrimary
        } else {
            R.color.textDarkColorPrimary
        }

    fun cardBackground(isDarkMode: Boolean) =
        if (isDarkMode) {
            R.color.standingsDarkCardBackground
        } else {
            R.color.standingsCardBackground
        }
}
