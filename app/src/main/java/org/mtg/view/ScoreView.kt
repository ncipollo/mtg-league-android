package org.mtg.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.score_view.view.*
import org.mtg.R

class ScoreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    init {
        View.inflate(context, R.layout.score_view, this)
    }

    var scoreDeltaText: CharSequence
        get() = scoreDelta.text
        set(value) {
            scoreDelta.text = value
        }

    var scoreDeltaTextShown: Boolean
        get() = !scoreDelta.isGone
        set(value) {
            scoreDelta.isGone = !value
        }

    var scoreText: CharSequence
        get() = score.text
        set(value) {
            score.text = value
        }

    fun setBackgroundRes(@DrawableRes res: Int) {
        score_background.setBackgroundResource(res)
    }

    fun setDecrementListener(listener:(View) -> Unit) {
        decrement_click.setOnClickListener { listener(it) }
    }

    fun setIncrementListener(listener:(View) -> Unit) {
        increment_click.setOnClickListener { listener(it) }
    }
}
