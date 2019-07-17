package org.mtg.app

import android.app.Application
import org.mtg.di.ApplicationInjector

class MagicLeagueApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ApplicationInjector.inject(this)
    }
}
