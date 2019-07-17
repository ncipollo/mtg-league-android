package org.mtg.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

object ApplicationInjector {
    fun inject(context: Context) {
        startKoin {
            androidContext(context)
            modules(Modules().modules)
        }
    }
}
