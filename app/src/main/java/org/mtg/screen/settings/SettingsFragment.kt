package org.mtg.screen.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.mtg.R
import org.mtg.model.Settings

class SettingsFragment : Fragment() {
    private companion object {
        private const val DARK_MODE_KEY = "darkMode"
        private const val PROGRESS_KEY = "progress"
    }

    private val viewModel: SettingsViewModel by viewModel()

    private var settings = Settings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val progress = findPreference<Preference>(PROGRESS_KEY)
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            settings = viewState.settings
            dark_mode_switch.isChecked = settings.darkMode
            // progress?.isVisible = viewState.inProgress
        })

        dark_mode_preference.setOnClickListener { handleDarkModeSwitch() }
        dark_mode_switch.setOnClickListener { handleDarkModeSwitch() }
    }

    private fun handleDarkModeSwitch() {
        val event = if (dark_mode_switch.isChecked) {
            SettingsViewEvent.Update(settings.copy(darkMode = true))
        } else {
            SettingsViewEvent.Update(settings.copy(darkMode = false))
        }
        viewModel.sendViewEvent(event)
        requireActivity().recreate()
    }
}
