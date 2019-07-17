package org.mtg.screen.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.bottom_navigation.bottom_navigation
import kotlinx.android.synthetic.main.fragment_standings.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.mtg.R
import org.mtg.arch.ItemListAdapter
import org.mtg.util.BottomNavigationHelper

class StandingsFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_standings, container, false)

    private val bottomNavigationHelper: BottomNavigationHelper by inject { parametersOf(requireActivity()) }
    private val itemsAdapter: ItemListAdapter by inject()
    private val viewModel: StandingsViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation.selectedItemId = R.id.navigation_standings

        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottomNavigationHelper.setupBottomNavigationTheme(view.context, bottom_navigation)
        standings_recycler_view.adapter = itemsAdapter
        viewModel.standings().observe(viewLifecycleOwner, Observer { state->
            when (state) {
                is StandingsViewModel.StandingsViewState.Success -> itemsAdapter.submitList(state.items)
                 else -> {}
            }

        })
    }

    override fun onNavigationItemSelected(item: MenuItem) =
        bottomNavigationHelper.onNavigationItemSelected(item, findNavController())
}
