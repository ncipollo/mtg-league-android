package org.mtg.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.scope.Scope
import org.koin.dsl.bind
import org.koin.dsl.module
import org.mtg.Database
import org.mtg.api.*
import org.mtg.repository.LeagueRemoteRepository
import org.mtg.repository.SettingsLocalRepository
import org.mtg.repository.StandingRemoteRepository
import org.mtg.util.BottomNavigationHelper
import org.rx.MagicSchedulers
import org.rx.ProductionSchedulers

class CommonModules : Modules {

    override val modules = listOf(
        api(),
        database(),
        helpers(),
        repos(),
        schedulers()
    )

    private fun api() = module {
        single { ApiFactory(get()) }

        single { createApi<LeagueApi>(get()) }
        single { createApi<MatchResultApi>(get()) }
        single { createApi<StandingApi>(get()) }
        single { createApi<UserApi>(get()) }
    }

    private fun database() = module {
        single { AndroidSqliteDriver(Database.Schema, get(), "mtg.db") } bind SqlDriver::class
        single { Database(get()) }
        single { db().settingsQueries }
    }

    private fun helpers() = module {
        factory { BottomNavigationHelper(get()) }
    }

    private fun repos() = module {
        factory { LeagueRemoteRepository(get()) }
        factory { StandingRemoteRepository(get()) }
        factory { SettingsLocalRepository(get(), get()) }
    }

    private fun schedulers() = module {
        factory { ProductionSchedulers() } bind MagicSchedulers::class
    }

    private inline fun <reified T> createApi(apiFactory: ApiFactory) = apiFactory.createApi<T>()

    private fun Scope.db(): Database = get()

}
