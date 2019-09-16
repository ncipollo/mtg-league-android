package org.mtg.screen.settings

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.mtg.model.Settings
import org.mtg.repository.SettingsLocalRepository

class SettingsUseCase(private val settingsRepo: SettingsLocalRepository) {
    sealed class Action {
        data class Save(val settings: Settings) : Action()
        object Fetch : Action()
        data class Update(
            val selectedLeagueId: Long = 0,
            val selectedLeagueName: String = ""
        ) : Action()
    }

    sealed class Result {
        object Saved : Result()
        data class Retrieved(val settings: Settings) : Result()
        object InProgress : Result()
    }

    fun create() = ObservableTransformer<Action, Result> { source ->
        source.switchMap<Result> {
            when (it) {
                is Action.Fetch -> fetch()
                is Action.Save -> save(it)
                is Action.Update -> update(it)
            }.startWith(Result.InProgress)
        }
    }

    private fun fetch(): Observable<Result> =
        settingsRepo.get().map { Result.Retrieved(it) }

    private fun save(action: Action.Save): Observable<Result> =
        settingsRepo.update(action.settings)
            .andThen(Observable.just<Result>(Result.Saved))

    private fun update(action: Action.Update): Observable<Result> =
        settingsRepo.get()
            .take(1)
            .map {
                it.copy(
                    selectedLeagueId = action.selectedLeagueId,
                    selectedLeagueName = action.selectedLeagueName
                )
            }
            .switchMapCompletable { settingsRepo.update(it) }
            .andThen(Observable.just<Result>(Result.Saved))

}
