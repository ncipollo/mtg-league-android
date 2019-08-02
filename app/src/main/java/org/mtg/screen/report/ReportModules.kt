package org.mtg.screen.report

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import org.mtg.di.Modules

class ReportModules : Modules {
    override val modules: List<Module> = listOf(reportModule())

    private fun reportModule() = module {
        factory { ReportUseCase(get(), get()) }
        viewModel { ReportViewModel(get(), get()) }
    }
}
