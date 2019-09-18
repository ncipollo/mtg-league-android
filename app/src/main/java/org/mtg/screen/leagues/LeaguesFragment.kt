package org.mtg.screen.leagues

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_leagues.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.mtg.R
import org.mtg.arch.ItemAction
import org.mtg.arch.ItemActionListener
import org.mtg.arch.ItemListAdapter

class LeaguesFragment : Fragment() {
    private val loadingView get() = leagues_loading
    private val recyclerView get() = leagues_recycler_view

    private val actionListener: ItemActionListener = { routeItemAction(it) }
    private val adapter: ItemListAdapter by inject(LeagueModules.ADAPTER) {
        parametersOf(actionListener)
    }
    private val viewModel: LeaguesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_leagues, container, false)

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.adapter = adapter
        viewModel.viewState.observe(viewLifecycleOwner) {
            loadingView.isGone = !it.loading
            adapter.submitList(it.items)
        }
        viewModel.navigationEvents.observe(viewLifecycleOwner) {
            requireActivity().nav_host_fragment.findNavController().popBackStack()
        }
    }

    private fun routeItemAction(itemAction: ItemAction) {
        val item = itemAction.item
        if(item is LeagueItem) {
            viewModel.sendViewEvent(LeaguesViewEvent.Selected(item.leagueId, item.leagueName))
        }
    }
}
