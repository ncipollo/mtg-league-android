package org.mtg.screen.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import kotlinx.android.synthetic.main.fragment_scoreboard.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.mtg.R
import org.mtg.screen.HomeFragment
import org.mtg.screen.color.ColorPickerFragment
import org.mtg.screen.color.ColorPickerViewModel
import org.mtg.view.ScoreView
import org.mtg.viewmodel.activityViewModel

class PlayFragment : HomeFragment() {
    private companion object {
        const val BOTTOM_PLAYER_TARGET = "bottom_player"
        const val TOP_PLAYER_TARGET = "top_player"
    }

    private val bottomPlayerScore get() = bottom_player_score
    private val topPlayerScore get() = top_player_score
    private val scoreReset get() = score_reset

    private val viewModel: PlayViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_scoreboard, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupScoreBoard()
        setupViewModels()
    }

    private fun setupScoreBoard() {
        bottomPlayerScore.setDecrementListener {
            viewModel.sendViewEvent(PlayViewEvent.DecrementBottomPlayer)
        }
        bottomPlayerScore.setIncrementListener {
            viewModel.sendViewEvent(PlayViewEvent.IncrementBottomPlayer)
        }
        bottomPlayerScore.setColorPickerListener {
            ColorPickerFragment.show(childFragmentManager, BOTTOM_PLAYER_TARGET)
        }

        topPlayerScore.setDecrementListener {
            viewModel.sendViewEvent(PlayViewEvent.DecrementTopPlayer)
        }
        topPlayerScore.setIncrementListener {
            viewModel.sendViewEvent(PlayViewEvent.IncrementTopPlayer)
        }
        topPlayerScore.setColorPickerListener {
            ColorPickerFragment.show(childFragmentManager, TOP_PLAYER_TARGET)
        }

        scoreReset.setOnClickListener {
            viewModel.sendViewEvent(PlayViewEvent.Reset(20))
        }
    }

    private fun setupViewModels() {
        viewModel.viewState.observe(viewLifecycleOwner) {
            bottomPlayerScore.applyViewState(it.bottomScore)
            topPlayerScore.applyViewState(it.topScore)
        }

        activityViewModel<ColorPickerViewModel>()
            .colorChanged
            .observe(viewLifecycleOwner) { (color, target) ->
                when (target) {
                    BOTTOM_PLAYER_TARGET -> viewModel.sendViewEvent(
                        PlayViewEvent.UpdateBottomColor(color)
                    )
                    TOP_PLAYER_TARGET -> viewModel.sendViewEvent(
                        PlayViewEvent.UpdateTopColor(color)
                    )
                }
            }
    }

    private fun ScoreView.applyViewState(viewState: ScoreViewState) {
        setBackgroundRes(viewState.backgroundColor)
        scoreText = viewState.scoreText
        scoreDeltaText = viewState.scoreDeltaText
        scoreDeltaTextShown = viewState.showDelta
        toggleBrightColorPicker(viewState.brightColorPicker)
    }
}
