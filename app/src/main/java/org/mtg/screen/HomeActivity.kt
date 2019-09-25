package org.mtg.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject
import org.mtg.R
import org.mtg.util.BottomNavigationHelper

class HomeActivity : AppCompatActivity() {
    private companion object {
        val TOP_LEVEL_SCREENS = setOf(
            R.id.standingsFragment,
            R.id.playFragment,
            R.id.reportFragment
        )
    }

    private val bottomNavigation get() = bottom_navigation
    private val bottomNavigationHelper: BottomNavigationHelper by inject()
    private val navController get() = findNavController(R.id.nav_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupBottomNavigationView()
        setupNavigationController()
    }

    private fun setupBottomNavigationView() {
        bottomNavigation.setOnNavigationItemSelectedListener {
            it.isChecked = true
            bottomNavigationHelper.onNavigationItemSelected(it, navController)
        }
    }

    private fun setupNavigationController() {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_SCREENS)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
}
