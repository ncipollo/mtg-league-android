package org.mtg.screen.play

import org.junit.Test
import org.mtg.R
import org.mtg.model.Player
import org.mtg.model.Score
import kotlin.test.assertEquals

class ScoreViewStateFactoryTest {
    private companion object {
        val COLOR = R.color.magic_green
        const val LIFE = 15
        const val LIFE_DELTA = 2
    }

    private val factory = ScoreViewStateFactory()

    @Test
    fun fromPlayer_withDelta() {
        val score = Score(life = LIFE, lifeChange = LIFE_DELTA)
        val player = Player(score = score, color = COLOR)

        assertEquals(
            expected = ScoreViewState(
                backgroundColor = COLOR,
                scoreText = LIFE.toString(),
                scoreDeltaText = LIFE_DELTA.toString(),
                showDelta = true
            ),
            actual = factory.fromPlayer(player)
        )
    }

    @Test
    fun fromPlayer_withoutDelta() {
        val score = Score(life = LIFE)
        val player = Player(score = score, color = COLOR)

        assertEquals(
            expected = ScoreViewState(
                backgroundColor = COLOR,
                scoreText = LIFE.toString(),
                scoreDeltaText = "0",
                showDelta = false
            ),
            actual = factory.fromPlayer(player)
        )
    }
}
