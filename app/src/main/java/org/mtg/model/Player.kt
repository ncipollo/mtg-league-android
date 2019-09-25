package org.mtg.model

import androidx.annotation.DrawableRes
import org.mtg.R

data class Player(val score: Score = Score(), @DrawableRes val color: Int = R.color.magic_white) {
    operator fun plus(value: Int) = copy(score = score + value)
    operator fun minus(value: Int) = copy(score = score - value)
}


