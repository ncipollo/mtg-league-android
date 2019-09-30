package org.mtg.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import org.mtg.R
import org.mtg.domain.CurrentLeagueUseCase

abstract class HomeFragment : Fragment() {

    private val currentLeagueUseCase by inject<CurrentLeagueUseCase>()
    private var leagueDisposable: Disposable? = null

    private val homeActivity get() = activity as? HomeActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        observeCurrentLeague()
    }

    override fun onStop() {
        super.onStop()
        leagueDisposable?.dispose()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.main_menu, menu)


    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.settings_menu -> {
                findNavController().navigate(R.id.navigate_to_leagues)
                true
            }
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun observeCurrentLeague() {
        leagueDisposable?.dispose()

        currentLeagueUseCase.get()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { updateTitle(it.leagueName) }
            .also { leagueDisposable = it }
    }

    private fun updateTitle(leagueName: String) {
        val title = getString(R.string.toolbar_title_format, leagueName)
        homeActivity?.supportActionBar?.title = title
    }
}
