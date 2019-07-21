package org.mtg.screen.settings

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.mtg.model.Settings
import org.mtg.viewmodel.MagicViewModel

class SettingsViewModel(private val settingsUseCase: SettingsUseCase) : MagicViewModel() {
    private val events = PublishSubject.create<SettingsViewEvent>()

    fun sendViewEvent(event: SettingsViewEvent) = events.onNext(event)

    val viewState = createViewState().toLiveData()

    private fun createViewState() =
        events.startWith(SettingsViewEvent.Fetch)
            .switchMap { routeEvent(it) }
            .map { resultToViewState(it) }

    private fun routeEvent(event: SettingsViewEvent) =
        when (event) {
            SettingsViewEvent.Fetch -> fetch()
            is SettingsViewEvent.Update -> update(event.settings)
        }

    private fun fetch() =
        Observable.just(SettingsUseCase.Action.Fetch)
            .compose(settingsUseCase.create())

    private fun update(settings: Settings) =
        Observable.just(SettingsUseCase.Action.Save(settings))
            .compose(settingsUseCase.create())
            .switchMap { fetch() }

    private fun resultToViewState(result: SettingsUseCase.Result) =
        when (result) {
            SettingsUseCase.Result.Saved -> throw IllegalStateException("Saved result should have been mapped to fetch")
            is SettingsUseCase.Result.Retrieved -> SettingsViewState(settings = result.settings, inProgress = false)
            SettingsUseCase.Result.InProgress -> SettingsViewState(inProgress = true)
        }
}

sealed class SettingsViewEvent {
    object Fetch : SettingsViewEvent()
    data class Update(val settings: Settings) : SettingsViewEvent()
}

data class SettingsViewState(val settings: Settings = Settings(), val inProgress: Boolean = false)
