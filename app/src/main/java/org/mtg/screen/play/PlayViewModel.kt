package org.mtg.screen.play

import androidx.annotation.DrawableRes
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.mtg.R
import org.mtg.model.PlayerColor
import org.mtg.model.ScoreBoard
import org.mtg.viewmodel.MagicViewModel

class PlayViewModel(
    private val playUseCase: PlayUseCase,
    private val scoreFactory: ScoreViewStateFactory
) : MagicViewModel() {
    private val viewEvents = eventChannel<PlayViewEvent>()

    val viewState =
        viewEvents.asFlow()
            .map { eventToAction(it) }
            .let { playUseCase.get(it) }
            .map { scoreboardToViewState(it.scoreBoard) }
            .asLiveData(context = viewModelScope.coroutineContext, timeoutInMs = Long.MAX_VALUE)

    fun sendViewEvent(event: PlayViewEvent) = viewModelScope.launch {
        viewEvents.send(event)
    }

    private fun eventToAction(event: PlayViewEvent) =
        when (event) {
            PlayViewEvent.DecrementBottomPlayer -> PlayUseCase.Action.DecrementBottomPlayer
            PlayViewEvent.DecrementTopPlayer -> PlayUseCase.Action.DecrementTopPlayer
            PlayViewEvent.IncrementBottomPlayer -> PlayUseCase.Action.IncrementBottomPlayer
            PlayViewEvent.IncrementTopPlayer -> PlayUseCase.Action.IncrementTopPlayer
            is PlayViewEvent.Reset -> PlayUseCase.Action.Reset(event.life)
            is PlayViewEvent.UpdateBottomColor -> PlayUseCase.Action.UpdateBottomColor(event.color)
            is PlayViewEvent.UpdateTopColor -> PlayUseCase.Action.UpdateTopColor(event.color)
        }

    private fun scoreboardToViewState(scoreBoard: ScoreBoard): PlayViewState =
        PlayViewState(
            bottomScore = scoreFactory.fromPlayer(scoreBoard.bottomPlayer),
            topScore = scoreFactory.fromPlayer(scoreBoard.topPlayer)
        )
}

sealed class PlayViewEvent {
    object DecrementBottomPlayer : PlayViewEvent()
    object DecrementTopPlayer : PlayViewEvent()
    object IncrementBottomPlayer : PlayViewEvent()
    object IncrementTopPlayer : PlayViewEvent()
    data class Reset(val life: Int = 20) : PlayViewEvent()
    data class UpdateBottomColor(val color: PlayerColor) : PlayViewEvent()
    data class UpdateTopColor(val color: PlayerColor) : PlayViewEvent()
}

data class PlayViewState(
    val bottomScore: ScoreViewState = ScoreViewState(),
    val topScore: ScoreViewState = ScoreViewState()
)

data class ScoreViewState(
    @DrawableRes val backgroundColor: Int = R.color.magic_white,
    val brightColorPicker: Boolean = false,
    val scoreText: String = "",
    val scoreDeltaText: String = "",
    val showDelta: Boolean = false
)
