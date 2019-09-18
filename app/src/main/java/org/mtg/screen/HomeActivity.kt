package org.mtg.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import org.mtg.R

class HomeActivity : AppCompatActivity() {
    private companion object {
        val TOP_LEVEL_SCREENS = setOf(
            R.id.standingsFragment,
            R.id.scoreboardFragment,
            R.id.reportFragment
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_SCREENS)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}
