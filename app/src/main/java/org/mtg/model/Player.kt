package org.mtg.model

data class Player(
    val score: Score = Score(),
    val color: PlayerColor = PlayerColor.WHITE
) {
    operator fun plus(value: Int) = copy(score = score + value)
    operator fun minus(value: Int) = copy(score = score - value)
}


