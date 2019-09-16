package org.mtg.screen.leagues

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mtg.arch.ItemActionListener
import org.mtg.arch.ItemListAdapter
import org.mtg.arch.itemViewHolderFactory
import org.mtg.di.Modules
import org.mtg.di.dependencyName

class LeagueModules : Modules {
    companion object {
        val ADAPTER = dependencyName<LeagueModules>("adapter")
    }

    override val modules: List<Module>
        get() = listOf(screen())

    private fun screen() = module {
        factory(ADAPTER) { (actionListener: ItemActionListener) ->
            createStandingsAdapter(
                actionListener
            )
        }
        factory { LeagueItemFactory() }
        factory { LeaguesUseCase(get()) }
        viewModel { LeaguesViewModel(get(), get(), get()) }
    }

    private fun createStandingsAdapter(actionListener: ItemActionListener): ItemListAdapter {
        val factory = itemViewHolderFactory {
            viewHolder { LeagueViewHolder(it, actionListener) }
        }
        return ItemListAdapter(factory)
    }
}
