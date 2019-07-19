package org.mtg.screen.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.mtg.viewmodel.MagicViewModel

class SettingsViewModel(private val settingsUseCase: SettingsUseCase) : MagicViewModel() {
    private val preferencesLiveData = MutableLiveData<SettingsViewState>()
    private val disposeBag = CompositeDisposable()

    sealed class SettingsViewState {
        data class Success(val darkMode: Boolean) : SettingsViewState()
        object InProgress : SettingsViewState()
        object Error : SettingsViewState()
    }

    fun saveDarkMode(value: Boolean) {
        disposeBag.add(
            Observable.just(SettingsUseCase.Action.Save(value))
                .compose(settingsUseCase.create()).subscribe()
        )
    }

    fun preferenceState(): LiveData<SettingsViewState> {
        disposeBag.add(
            Observable.just(SettingsUseCase.Action.Fetch)
                .compose(settingsUseCase.create())
                .startWith(SettingsUseCase.Result.InProgress)
                .subscribe { darkMode -> preferencesLiveData.postValue(mapResult(darkMode)) }
        )
        return preferencesLiveData
    }

    private fun mapResult(result: SettingsUseCase.Result) =
        when (result) {
            is SettingsUseCase.Result.Retrieved -> SettingsViewState.Success(result.value)
            SettingsUseCase.Result.InProgress -> SettingsViewState.InProgress
            else -> SettingsViewState.Error
        }

    override fun onCleared() {
        disposeBag.dispose()
    }
}
