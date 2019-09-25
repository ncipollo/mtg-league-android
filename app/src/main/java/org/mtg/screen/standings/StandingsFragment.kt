package org.mtg.screen.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_standings.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.mtg.R
import org.mtg.arch.ItemListAdapter
import org.mtg.screen.HomeFragment

class StandingsFragment : HomeFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_standings, container, false)

    private val itemsAdapter: ItemListAdapter by inject(StandingsModules.ADAPTER)
    private val viewModel: StandingsViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        standings_recycler_view.adapter = itemsAdapter
        viewModel.standings().observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is StandingsViewModel.StandingsViewState.Success -> successState(state)
                else -> standings_loading.isGone = false
            }
        })
    }

    private fun successState(state: StandingsViewModel.StandingsViewState.Success) {
        itemsAdapter.submitList(state.items)
        standings_loading.isGone = true
    }
}
