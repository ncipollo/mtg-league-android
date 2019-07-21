package org.mtg.screen.settings

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import org.mtg.model.Settings
import org.mtg.repository.SettingsLocalRepository

class SettingsUseCase(private val settingsRepo: SettingsLocalRepository) {
    sealed class Action {
        data class Save(val settings: Settings) : Action()
        object Fetch : Action()
    }

    sealed class Result {
        object Saved : Result()
        data class Retrieved(val settings: Settings) : Result()
        object InProgress : Result()
    }

    fun create() = ObservableTransformer<Action, Result> { source ->
        source.switchMap<Result> {
            when (it) {
                is Action.Save -> save(it)
                is Action.Fetch -> fetch()
            }.startWith(Result.InProgress)
        }
    }

    private fun save(action: Action.Save): Observable<Result> =
        settingsRepo.update(action.settings)
            .andThen(Observable.just<Result>(Result.Saved))

    private fun fetch(): Observable<Result> =
        settingsRepo.get().map { Result.Retrieved(it) }

}
