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

class SettingsFragment : Fragment() {
    private companion object {
        private const val DARK_MODE_KEY = "darkMode"
        private const val PROGRESS_KEY = "progress"
    }

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // val progress = findPreference<Preference>(PROGRESS_KEY)
        viewModel.preferenceState().observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is SettingsViewModel.SettingsViewState.Success -> {
                    dark_mode_switch.isChecked = viewState.darkMode
                    // progress?.isVisible = false
                }
                else -> {
                }//progress?.isVisible = true
            }
        })

        dark_mode_preference.setOnClickListener { handleDarkModeSwitch() }
        dark_mode_switch.setOnClickListener { handleDarkModeSwitch() }
    }

    private fun handleDarkModeSwitch() {
        if (dark_mode_switch.isChecked) {
            viewModel.saveDarkMode(true)
        } else {
            viewModel.saveDarkMode(false)
        }
        requireActivity().recreate()
    }
}
