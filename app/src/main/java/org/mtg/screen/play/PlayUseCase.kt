package org.mtg.screen.play

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import org.mtg.model.Player
import org.mtg.model.PlayerColor
import org.mtg.model.Score
import org.mtg.model.ScoreBoard
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

class PlayUseCase {
    companion object {
        val LIFE_CHANGE_LIFE_SPAN = TimeUnit.SECONDS.toMillis(5)
    }

    sealed class Action {
        object ClearLifeChanges : Action()
        object DecrementBottomPlayer : Action()
        object DecrementTopPlayer : Action()
        object IncrementBottomPlayer : Action()
        object IncrementTopPlayer : Action()
        data class Reset(val life: Int = 20) : Action()
        data class UpdateBottomColor(val color: PlayerColor) : Action()
        data class UpdateTopColor(val color: PlayerColor) : Action()
    }

    data class Result(val scoreBoard: ScoreBoard)

    // Hacky in memory scoreboard cache
    private val scoreBoardRef = AtomicReference<ScoreBoard>(ScoreBoard())

    fun get(upstream: Flow<Action>) =
        upstream
            .flatMapLatest { action ->
                flow {
                    emit(action)
                    if (action !is Action.Reset) {
                        delay(LIFE_CHANGE_LIFE_SPAN)
                        emit(Action.ClearLifeChanges)
                    }
                }
            }
            .scan(scoreBoardRef.get()) { scoreboard, action -> accumulate(scoreboard, action) }
            .onEach { scoreBoardRef.set(clearLifeChanges(it)) }
            .map { Result(it) }

    private fun accumulate(scoreBoard: ScoreBoard, action: Action) =
        when (action) {
            Action.ClearLifeChanges -> clearLifeChanges(scoreBoard)
            Action.DecrementBottomPlayer -> updateBottomPlayerScore(scoreBoard, -1)
            Action.DecrementTopPlayer -> updateTopPlayerScore(scoreBoard, -1)
            Action.IncrementBottomPlayer -> updateBottomPlayerScore(scoreBoard, 1)
            Action.IncrementTopPlayer -> updateTopPlayerScore(scoreBoard, 1)
            is Action.Reset -> reset(scoreBoard, action.life)
            is Action.UpdateBottomColor -> updateBottomPlayerColor(scoreBoard, action.color)
            is Action.UpdateTopColor -> updateTopPlayerColor(scoreBoard, action.color)
        }

    private fun clearLifeChanges(scoreBoard: ScoreBoard): ScoreBoard {
        val bottomPlayer = clearPlayerLifeChanges(scoreBoard.bottomPlayer)
        val topPlayer = clearPlayerLifeChanges(scoreBoard.topPlayer)
        return ScoreBoard(bottomPlayer, topPlayer)
    }

    private fun clearPlayerLifeChanges(player: Player) =
        player.copy(score = player.score.copy(lifeChange = 0))

    private fun updateBottomPlayerScore(scoreBoard: ScoreBoard, value: Int) =
        scoreBoard.copy(bottomPlayer = updatePlayerScore(scoreBoard.bottomPlayer, value))

    private fun updateTopPlayerScore(scoreBoard: ScoreBoard, value: Int) =
        scoreBoard.copy(topPlayer = updatePlayerScore(scoreBoard.topPlayer, value))

    private fun updatePlayerScore(player: Player, value: Int) = player + value

    private fun reset(scoreBoard: ScoreBoard, life: Int): ScoreBoard {
        val bottomPlayer = resetPlayerScore(scoreBoard.bottomPlayer, life)
        val topPlayer = resetPlayerScore(scoreBoard.topPlayer, life)
        return ScoreBoard(bottomPlayer, topPlayer)
    }

    private fun updateBottomPlayerColor(scoreBoard: ScoreBoard, color: PlayerColor) =
        scoreBoard.copy(bottomPlayer = updatePlayerColor(scoreBoard.bottomPlayer, color))

    private fun updateTopPlayerColor(scoreBoard: ScoreBoard, color: PlayerColor) =
        scoreBoard.copy(topPlayer = updatePlayerColor(scoreBoard.topPlayer, color))

    private fun updatePlayerColor(player: Player, color: PlayerColor) =
        player.copy(color = color)

    private fun resetPlayerScore(player: Player, life: Int) = player.copy(Score(life = life))
}
