package org.mtg.repository

import com.squareup.sqldelight.runtime.rx.asObservable
import com.squareup.sqldelight.runtime.rx.mapToList
import io.reactivex.Completable
import io.reactivex.Observable
import org.mtg.entity.SettingsEntity
import org.mtg.entity.SettingsQueries
import org.mtg.model.Settings
import org.rx.MagicSchedulers

class SettingsLocalRepository(private val settingsQueries: SettingsQueries, private val schedulers: MagicSchedulers) {
    companion object {
        const val ID = 1L
    }

    fun darkMode() = settingsQueries.get(ID).executeAsOneOrNull()?.darkMode ?: false

    fun get(): Observable<Settings> =
        settingsQueries.get(ID)
            .asObservable(schedulers.io)
            .mapToList()
            .map {
                settingsFromEntities(it)
            }

    private fun settingsFromEntities(entities: List<SettingsEntity>) =
        entities.firstOrNull()
            ?.let {
                Settings(
                    darkMode = it.darkMode,
                    selectedLeagueId = it.selectedLeagueId,
                    selectedLeagueName = it.selectedLeagueName
                )
            } ?: Settings()

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
