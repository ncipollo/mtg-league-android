package org.mtg.screen.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.bottom_navigation.bottom_navigation
import kotlinx.android.synthetic.main.fragment_report.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.mtg.R
import org.mtg.util.BottomNavigationHelper
import org.mtg.util.KeyboardHelper


class ReportFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {
    private companion object {
        const val AUTO_COMPLETE_LAYOUT = android.R.layout.simple_dropdown_item_1line

        val PRESET_NUMBER_OF_GAMES = listOf("3", "2")
    }

    private val gamesText get() = report_games_text
    private val loserText get() = report_loser_text
    private val winnerText get() = report_winner_text

    private val submitButton get() = report_submit_button

    private val nameToUserId = hashMapOf<String, Long>()
    private val viewModel by viewModel<ReportViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_report, container, false)

    private val bottomNavigationHelper: BottomNavigationHelper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { _, _ -> KeyboardHelper.hideSoftInput(view) }
        setupBottomNavigation(view)
        setupNumberOfGames()
        setupSubmitButton()
        setupViewModel()
    }

    private fun setupBottomNavigation(view: View) {
        bottom_navigation.selectedItemId = R.id.navigation_report
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottomNavigationHelper.setupBottomNavigationTheme(view.context, bottom_navigation)
    }

    private fun setupNumberOfGames() {
        gamesText.setAdapter(
            ArrayAdapter(
                requireContext(),
                AUTO_COMPLETE_LAYOUT,
                PRESET_NUMBER_OF_GAMES
            )
        )
    }

    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            val winnerName = winnerText.text.toString()
            val loserName = loserText.text.toString()
            val winnerId = nameToUserId[winnerName]
            val loserId = nameToUserId[loserName]
            val games = gamesText.text.toString().toLongOrNull()

            validateWinner(winnerId)
            validateLoser(loserId)
            validateGames(games)

            if (winnerId != null && loserId != null && games != null) {
                val event = ReportViewEvent(winnerId = winnerId, loserId = loserId, gamesCount = games)
                viewModel.sendEvent(event)
            }
        }
    }

    private fun validateWinner(id: Long?) {
        winnerText.error = if (id == null) {
             "Please select a user in the league"
        } else {
            null
        }
    }

    private fun validateLoser(id: Long?) {
        loserText.error = if (id == null) {
            "Please select a user in the league"
        } else {
            null
        }
    }

    private fun validateGames(id: Long?) {
        gamesText.error = if (id == null) {
            "Please select a user in the league"
        } else {
            null
        }
    }

    private fun setupViewModel() {
        viewModel.viewState.observe(viewLifecycleOwner) {
            applyViewState(it)
        }
        viewModel.snackText.observe(viewLifecycleOwner) {
            applySnack(it)
        }
    }

    private fun applySnack(snackText: String) {
        Snackbar.make(submitButton, snackText, Snackbar.LENGTH_SHORT).show()
    }

    private fun applyViewState(viewState: ReportViewState) {
        updateNameToIdMapping(viewState.users)
        setupWinner(viewState.users)
        setupLoser(viewState.users)
        submitButton.isEnabled = viewState.enableSubmit
    }

    private fun updateNameToIdMapping(userItems: List<UserItem>) {
        nameToUserId.clear()
        nameToUserId += userItems.map { it.name to it.user.id }
    }

    private fun setupWinner(userItems: List<UserItem>) {
        winnerText.setAdapter(
            ArrayAdapter(
                requireContext(),
                AUTO_COMPLETE_LAYOUT,
                userItems.map { it.name }
            )
        )
    }

    private fun setupLoser(userItems: List<UserItem>) {
        loserText.setAdapter(
            ArrayAdapter(
                requireContext(),
                AUTO_COMPLETE_LAYOUT,
                userItems.map { it.name }
            )
        )
    }

    override fun onNavigationItemSelected(item: MenuItem) =
        bottomNavigationHelper.onNavigationItemSelected(item, findNavController())
}
