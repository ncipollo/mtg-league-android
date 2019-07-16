package org.mtg

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import org.mtg.api.*
import org.mtg.util.BottomNavigationHelper

class HomeActivity : AppCompatActivity() {
    private val leagueApi by inject<LeagueApi>()
    private val matchesApi by inject<MatchResultApi>()
    private val standingApi by inject<StandingApi>()
    private val userApi by inject<UserApi>()
    private val compositeDisposable = CompositeDisposable()

    private companion object {
        val TOP_LEVEL_SCREENS = setOf(R.id.standingsFragment, R.id.scoreboardFragment, R.id.reportFragment)
    }

    private val bottomNavigationHelper: BottomNavigationHelper by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(bottomNavigationHelper.themeId())
        setContentView(R.layout.activity_home)
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_SCREENS)
        setupActionBarWithNavController(navController, appBarConfiguration)
        testLeague()
        testMatches()
        testStanding()
        testUser()
    }

    private fun testLeague() {
        leagueApi.leagues()
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    private fun testMatches() {
        matchesApi.matchResults()
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }

        leagueApi.matchResultsForLeague(8)
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }

        userApi.matchResultsForUser(1)
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    private fun testStanding() {
        standingApi.standingForLeague(8)
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    private fun testUser() {
        userApi.users()
            .onApiErrorReturn { emptyList() }
            .subscribeBy { println(it) }
            .also { compositeDisposable.add(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.settings_menu -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.settingsFragment)
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
