package org.mtg.di

import androidx.appcompat.app.AppCompatActivity
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.mtg.api.*
import org.mtg.settings.SettingsUseCase
import org.mtg.settings.SettingsViewModel
import org.mtg.util.BottomNavigationHelper

class Modules {

    val modules = listOf(api(), helpers(), settingsScreen())

    private fun api() = module {
        single { ApiFactory(get()) }

        single { createApi<LeagueApi>(get()) }
        single { createApi<MatchResultApi>(get()) }
        single { createApi<StandingApi>(get()) }
        single { createApi<UserApi>(get()) }
    }

    private fun helpers() = module {
        factory { (activity: AppCompatActivity) -> BottomNavigationHelper(activity) }
    }

    private fun settingsScreen() = module {
        factory { SettingsUseCase() }
        viewModel { SettingsViewModel(get()) }
    }

    private inline fun <reified T> createApi(apiFactory: ApiFactory) = apiFactory.createApi<T>()
}
