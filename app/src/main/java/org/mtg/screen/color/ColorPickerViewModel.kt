package org.mtg.screen.color

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import org.mtg.model.PlayerColor
import org.mtg.viewmodel.MagicViewModel

class ColorPickerViewModel: MagicViewModel() {
    private val colorEvent = eventChannel<ColorEvent>()

    val colorChanged =
        colorEvent.asFlow()
            .asLiveData(context = viewModelScope.coroutineContext, timeoutInMs = Long.MAX_VALUE)

    fun pickColor(color: PlayerColor, targetPlayer: String) = viewModelScope.launch {
        colorEvent.send(ColorEvent(color, targetPlayer))
    }
}

data class ColorEvent(val color: PlayerColor = PlayerColor.WHITE, val targetPlayer: String = "")
