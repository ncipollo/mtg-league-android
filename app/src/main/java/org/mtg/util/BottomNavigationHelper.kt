package org.mtg.util

import android.content.Context
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mtg.R

class BottomNavigationHelper(private val darkModeEnabled: Boolean) {

    fun setupBottomNavigationTheme(context: Context, view: BottomNavigationView) {
        view.itemIconTintList =
            ContextCompat.getColorStateList(context, bottomNavigationColorStateList())
        view.itemTextColor =
            ContextCompat.getColorStateList(context, bottomNavigationColorStateList())
        val color = if (darkModeEnabled) {
            R.color.colorAccent
        } else {
            R.color.colorPrimary
        }
        view.setBackgroundColor(ContextCompat.getColor(context, color))
    }

    private fun bottomNavigationColorStateList() =
        if (darkModeEnabled) {
            R.color.navigation_icon_background_dark
        } else {
            R.color.navigation_icon_background_light
        }
    
    fun themeId() =
        if (darkModeEnabled) {
            R.style.AppThemeDark
        } else {
            R.style.AppThemeLight
        }

    fun menuItemColor() =
        if (darkModeEnabled) {
            R.color.colorPrimary
        } else {
            R.color.colorAccent
        }

    fun onNavigationItemSelected(item: MenuItem, navController: NavController): Boolean {
        when (item.itemId) {
            R.id.navigation_play ->
                navController.navigate(R.id.scoreboardFragment)
            R.id.navigation_standings ->
                navController.navigate(R.id.standingsFragment)
            R.id.navigation_report ->
                navController.navigate(R.id.reportFragment)
        }
        return true
    }

    fun backgroundColor() =
        if (darkModeEnabled) {
            R.color.standingsDarkBackground
        } else {
            R.color.standingsBackground
        }
}
