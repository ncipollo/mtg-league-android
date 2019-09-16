package org.mtg.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import org.koin.android.ext.android.inject
import org.mtg.R
import org.mtg.util.BottomNavigationHelper

class HomeActivity : AppCompatActivity() {
    private companion object {
        val TOP_LEVEL_SCREENS = setOf(
            R.id.standingsFragment,
            R.id.scoreboardFragment,
            R.id.reportFragment
        )
    }

    private val bottomNavigationHelper: BottomNavigationHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(bottomNavigationHelper.themeId())
        setContentView(R.layout.activity_home)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_SCREENS)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.settings_menu -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.leaguesFragment)
                true
            }
            android.R.id.home -> {
                findNavController(R.id.nav_host_fragment).popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val icon = menu.findItem(R.id.settings_menu).icon
        icon.setTint(ContextCompat.getColor(this, bottomNavigationHelper.menuItemColor()))
        menu.findItem(R.id.settings_menu).icon = icon
        return super.onCreateOptionsMenu(menu)
    }
}
