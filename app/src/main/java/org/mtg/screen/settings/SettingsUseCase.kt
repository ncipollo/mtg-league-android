package org.mtg.screen.settings

import io.reactivex.ObservableTransformer

class SettingsUseCase {
    sealed class Action {
        data class Save(val value: Boolean) : Action()
        object Fetch : Action()
    }

    sealed class Result {
        object Saved : Result()
        data class Retrieved(val value: Boolean) : Result()
        object InProgress: Result()
    }

    fun create() = ObservableTransformer<Action, Result> { source ->
        source.map { action ->
            when (action) {
                is Action.Save -> {
                    SharedPreferencesUtil.saveDarkMode(action.value)
                    Result.Saved
                }
                is Action.Fetch -> {
                    val darkMode = SharedPreferencesUtil.darkMode()
                    Result.Retrieved(darkMode)
                }
            }
        }.startWith(Result.InProgress)
    }
}
