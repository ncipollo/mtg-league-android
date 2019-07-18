package org.mtg.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mtg.api.*
import org.mtg.arch.ItemListAdapter
import org.mtg.arch.itemViewHolderFactory
import org.mtg.repository.LeagueRemoteRepository
import org.mtg.repository.StandingRemoteRepository
import org.mtg.screen.settings.SettingsUseCase
import org.mtg.screen.settings.SettingsViewModel
import org.mtg.screen.settings.SharedPreferencesUtil
import org.mtg.screen.standings.StandingViewHolder
import org.mtg.screen.standings.StandingsUseCase
import org.mtg.screen.standings.StandingsViewModel
import org.mtg.util.BottomNavigationHelper

class Modules {

    val modules = listOf(api(), helpers(), settingsScreen(), standingsScreen())

    private fun api() = module {
        single { ApiFactory(get()) }

        single { createApi<LeagueApi>(get()) }
        single { createApi<MatchResultApi>(get()) }
        single { createApi<StandingApi>(get()) }
        single { createApi<UserApi>(get()) }

        factory { LeagueRemoteRepository(get()) }
        factory { StandingRemoteRepository(get()) }
    }

    private fun helpers() = module {
        factory { (activity: AppCompatActivity) -> BottomNavigationHelper(activity) }
    }

    private fun settingsScreen() = module {
        factory { SettingsUseCase() }
        viewModel { SettingsViewModel(get()) }
    }

    private fun standingsScreen() = module {
        factory { createStandingsAdapter() }
        factory { StandingsUseCase(get()) }
        viewModel { StandingsViewModel(get(), get()) }
    }

    private fun createStandingsAdapter(): ItemListAdapter {
        val factory = itemViewHolderFactory {
            viewHolder { StandingViewHolder(it, isDarkMode(it.context)) }
        }
        return ItemListAdapter(factory)
    }

    private fun isDarkMode(context: Context) = SharedPreferencesUtil.darkMode(context)


    private inline fun <reified T> createApi(apiFactory: ApiFactory) = apiFactory.createApi<T>()
}
