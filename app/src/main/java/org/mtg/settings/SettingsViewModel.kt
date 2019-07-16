package org.mtg.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class SettingsViewModel(private val settingsUseCase: SettingsUseCase) : ViewModel() {
    private val darkModeLiveData = MutableLiveData<Boolean>()
    private val disposeBag = CompositeDisposable()

    fun saveDarkMode(context: Context, value: Boolean) {
        disposeBag.add(
            Single.just(SettingsUseCase.Action.Save(context, value))
                .compose(settingsUseCase.create()).subscribe()
        )
    }

    fun darkMode(context: Context): LiveData<Boolean> {
        disposeBag.add(
            Single.just(SettingsUseCase.Action.Fetch(context))
                .compose(settingsUseCase.create())
                .subscribe { darkMode ->
                    darkModeLiveData.postValue((darkMode as SettingsUseCase.Result.Retrieved?)?.value)
                }
        )
        return darkModeLiveData
    }

    override fun onCleared() {
        disposeBag.dispose()
    }
}
