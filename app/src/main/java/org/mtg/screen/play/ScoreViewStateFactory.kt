package org.mtg.screen.play

import org.mtg.model.Player

class ScoreViewStateFactory {
    fun fromPlayer(player: Player) =
        ScoreViewState(
            backgroundColor = player.color,
            scoreText = player.score.life.toString(),
            scoreDeltaText = player.score.lifeChange.toString(),
            showDelta = player.score.lifeChange != 0
        )
}
