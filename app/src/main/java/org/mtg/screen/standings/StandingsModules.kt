package org.mtg.screen.standings

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mtg.arch.ItemListAdapter
import org.mtg.arch.itemViewHolderFactory
import org.mtg.di.Modules
import org.mtg.repository.SettingsLocalRepository

class StandingsModules: Modules {
    override val modules: List<Module> = listOf(standingsScreen())

    private fun standingsScreen() = module {
        factory { createStandingsAdapter(get()) }
        viewModel { StandingsViewModel(get(), get()) }
    }

    private fun createStandingsAdapter(repo: SettingsLocalRepository): ItemListAdapter {
        val darkMode = repo.darkMode()
        val factory = itemViewHolderFactory {
            viewHolder { StandingViewHolder(it, darkMode) }
        }
        return ItemListAdapter(factory)
    }
}
