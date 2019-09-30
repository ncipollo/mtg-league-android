package org.mtg.util

import android.view.MenuItem
import androidx.navigation.NavController
import org.mtg.R

class BottomNavigationHelper {

    fun onNavigationItemSelected(item: MenuItem, navController: NavController): Boolean {
        when (item.itemId) {
            R.id.navigation_play ->
                navController.navigate(R.id.navigate_to_play)
            R.id.navigation_standings ->
                navController.navigate(R.id.navigate_to_standings)
            R.id.navigation_report ->
                navController.navigate(R.id.navigate_to_report)
        }
        return true
    }
}
