package org.mtg.screen.settings

import android.content.Context
import io.reactivex.SingleTransformer

class SettingsUseCase {
    sealed class Action {
        data class Save(val context: Context, val value: Boolean) : Action()
        data class Fetch(val context: Context) : Action()
    }

    sealed class Result {
        object Saved : Result()
        data class Retrieved(val value: Boolean) : Result()
    }

    fun create() = SingleTransformer<Action, Result> { source ->
        source.map { action ->
            when (action) {
                is Action.Save -> {
                    SharedPreferencesUtil.saveDarkMode(action.context, action.value)
                    Result.Saved
                }
                is Action.Fetch -> {
                    val darkMode = SharedPreferencesUtil.darkMode(action.context)
                    Result.Retrieved(darkMode)
                }
            }
        }
    }
}
