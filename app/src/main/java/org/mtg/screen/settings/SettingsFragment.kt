package org.mtg.screen.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import org.koin.android.viewmodel.ext.android.viewModel
import org.mtg.R

class SettingsFragment : PreferenceFragmentCompat() {
    private companion object {
        private const val DARK_MODE_KEY = "darkMode"
    }
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val darkModeSwitch: SwitchPreferenceCompat? = findPreference<SwitchPreferenceCompat>(DARK_MODE_KEY)
        viewModel.darkMode(context!!).observe(viewLifecycleOwner, Observer {
            darkModeSwitch?.isChecked = it
        })
        listView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.settings_background))

        darkModeSwitch?.setOnPreferenceClickListener { item ->
            if ((item as SwitchPreferenceCompat?)?.isChecked == true) {
                viewModel.saveDarkMode(context!!, true)
            } else {
                viewModel.saveDarkMode(context!!, false)
            }
            requireActivity().recreate()
            true
        }
    }
}
