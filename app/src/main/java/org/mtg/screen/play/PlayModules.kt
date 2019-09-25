package org.mtg.screen.play

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mtg.di.Modules

class PlayModules : Modules {
    override val modules: List<Module>
        get() = listOf(playModule())

    private fun playModule() = module {
        single { PlayUseCase() }
        factory { ScoreViewStateFactory() }
        viewModel { PlayViewModel(get(), get()) }
    }

}
