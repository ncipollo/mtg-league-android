package org.mtg.screen.settings

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mtg.di.Modules

class SettingsModules: Modules {
    override val modules: List<Module> = listOf(settingsScreen())

    private fun settingsScreen() = module {
        factory { SettingsUseCase(get()) }
        viewModel { SettingsViewModel(get()) }
    }
}
