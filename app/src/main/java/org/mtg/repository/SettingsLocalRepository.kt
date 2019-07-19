package org.mtg.repository

import com.squareup.sqldelight.runtime.rx.asObservable
import com.squareup.sqldelight.runtime.rx.mapToList
import io.reactivex.Completable
import io.reactivex.Observable
import org.mtg.data.Settings
import org.mtg.data.SettingsQueries
import org.rx.MagicSchedulers

class SettingsLocalRepository(private val settingsQueries: SettingsQueries, private val schedulers: MagicSchedulers) {
    companion object {
        const val ID = 1L
    }

    fun get(): Observable<List<Settings>> =
        settingsQueries.get(ID)
            .asObservable(schedulers.io)
            .mapToList()

    fun update(settings: Settings): Completable =
        Completable.fromCallable {
            settingsQueries.delete(ID)
            settingsQueries.insert(
                id = ID,
                darkMode = settings.darkMode,
                selectedLeagueId = settings.selectedLeagueId,
                selectedLeagueName = settings.selectedLeagueName
            )
        }.subscribeOn(schedulers.io)
}
