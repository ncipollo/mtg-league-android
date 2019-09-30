package org.mtg.screen.play

import org.mtg.model.Player
import org.mtg.model.PlayerColor

class ScoreViewStateFactory {
    fun fromPlayer(player: Player) =
        ScoreViewState(
            backgroundColor = player.color.res,
            brightColorPicker = player.color == PlayerColor.BLACK,
            scoreText = player.score.life.toString(),
            scoreDeltaText = player.score.lifeChange.toString(),
            showDelta = player.score.lifeChange != 0
        )
}
