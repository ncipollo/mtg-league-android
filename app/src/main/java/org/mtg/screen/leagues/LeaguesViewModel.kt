package org.mtg.screen.leagues

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.asFlow
import org.mtg.arch.ItemViewState
import org.mtg.screen.settings.SettingsUseCase
import org.mtg.viewmodel.MagicViewModel

class LeaguesViewModel(
    private val itemFactory: LeagueItemFactory,
    private val leaguesUseCase: LeaguesUseCase,
    private val settingsUseCase: SettingsUseCase
) : MagicViewModel() {
    private val viewEvents = eventChannel<LeaguesViewEvent>()

    val viewState =
        viewEvents.asFlow()
            .onStart { emit(LeaguesViewEvent.Refresh) }
            .filterIsInstance<LeaguesViewEvent.Refresh>()
            .flatMapLatest { leaguesUseCase.get() }
            .map { resultToViewState(it) }
            .asLiveData(context = viewModelScope.coroutineContext, timeoutInMs = Long.MAX_VALUE)

    val navigationEvents =
        viewEvents.asFlow()
            .filterIsInstance<LeaguesViewEvent.Selected>()
            .flatMapLatest { updateSettings(it) }
            .map { LeaguesNavigationEvent.Back }
            .asLiveData(context = viewModelScope.coroutineContext, timeoutInMs = Long.MAX_VALUE)

    fun sendViewEvent(event: LeaguesViewEvent) = viewModelScope.launch {
        viewEvents.send(event)
    }

    private fun resultToViewState(result: LeaguesUseCase.Result) =
        when (result) {
            LeaguesUseCase.Result.InProgress -> LeaguesViewState(loading = true)
            is LeaguesUseCase.Result.Success ->
                LeaguesViewState(items = itemFactory.items(result.leagues))
        }

    private fun updateSettings(event: LeaguesViewEvent.Selected) =
        Observable.just(SettingsUseCase.Action.Update(event.leagueId, event.leagueName))
            .compose(settingsUseCase.create())
            .ofType<SettingsUseCase.Result.Saved>()
            .toFlowable(BackpressureStrategy.BUFFER)
            .asFlow()
}


sealed class LeaguesViewEvent {
    object Refresh : LeaguesViewEvent()
    data class Selected(val leagueId: Long, val leagueName: String) : LeaguesViewEvent()
}

data class LeaguesViewState(
    val loading: Boolean = false,
    val items: List<ItemViewState> = emptyList()
)

sealed class LeaguesNavigationEvent {
    object Back : LeaguesNavigationEvent()
}
