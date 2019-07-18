package org.mtg.app

import android.app.Application
import android.content.Context
import org.mtg.di.ApplicationInjector

class MagicLeagueApp : Application() {
    init {
        instance = this
    }
    companion object {
        private var instance: MagicLeagueApp? = null

        fun applicationContext(): Context = instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        ApplicationInjector.inject(this)
    }
}
