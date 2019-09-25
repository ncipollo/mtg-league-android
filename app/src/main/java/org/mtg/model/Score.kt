package org.mtg.model

data class Score(val life: Int = 20, val lifeChange: Int = 0) {
    operator fun plus(value: Int) = Score(life + value, lifeChange + value)
    operator fun minus(value: Int) = Score(life - value, lifeChange - value)
}
