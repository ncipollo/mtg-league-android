package org.mtg.screen.color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_color_picker.*
import org.mtg.R
import org.mtg.model.PlayerColor
import org.mtg.viewmodel.activityViewModel

class ColorPickerFragment : DialogFragment() {
    companion object {
        private const val TARGET_PLAYER_ARG = "target_player"

        fun show(fragmentManager: FragmentManager, targetPlayer: String) =
            ColorPickerFragment()
                .also { it.arguments = arguments(targetPlayer)  }
                .show(fragmentManager, ColorPickerFragment::class.java.name)

        private fun arguments(targetPlayer: String) =
            Bundle().also { it.putString(TARGET_PLAYER_ARG, targetPlayer) }
    }

    private val blackButton get() = color_black
    private val blueButton get() = color_blue
    private val greenButton get() = color_green
    private val redButton get() = color_red
    private val whiteButton get() = color_white

    private val targetPlayer
        get() = arguments?.getString(TARGET_PLAYER_ARG)
            ?: throw IllegalArgumentException("Missing target player")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_color_picker, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        blackButton.setOnClickListener { sendColor(PlayerColor.BLACK) }
        blueButton.setOnClickListener { sendColor(PlayerColor.BLUE) }
        greenButton.setOnClickListener { sendColor(PlayerColor.GREEN) }
        redButton.setOnClickListener { sendColor(PlayerColor.RED) }
        whiteButton.setOnClickListener { sendColor(PlayerColor.WHITE) }
    }

    private fun sendColor(color: PlayerColor) {
        val viewModel = activityViewModel<ColorPickerViewModel>()
        viewModel.pickColor(color, targetPlayer)
        dismiss()
    }
}
