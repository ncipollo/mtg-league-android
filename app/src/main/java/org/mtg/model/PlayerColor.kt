package org.mtg.model

import androidx.annotation.DrawableRes
import org.mtg.R

enum class PlayerColor(@DrawableRes val res: Int) {
    BLACK(R.color.magic_black),
    BLUE(R.color.magic_blue),
    GREEN(R.color.magic_green),
    RED(R.color.magic_red),
    WHITE(R.color.magic_white)
}
