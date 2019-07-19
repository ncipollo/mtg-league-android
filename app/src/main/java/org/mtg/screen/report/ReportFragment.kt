package org.mtg.screen.report

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.bottom_navigation.bottom_navigation
import kotlinx.android.synthetic.main.fragment_report.*
import org.koin.android.ext.android.inject
import org.mtg.R
import org.mtg.util.BottomNavigationHelper
import org.mtg.util.KeyboardHelper


class ReportFragment : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener {
    private companion object {
        val PRESET_NUMBER_OF_GAMES = listOf("3", "1")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_report, container, false)

    private val bottomNavigationHelper: BottomNavigationHelper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation.selectedItemId = R.id.navigation_report
        bottom_navigation.setOnNavigationItemSelectedListener(this)
        bottomNavigationHelper.setupBottomNavigationTheme(view.context, bottom_navigation)
        setDropdown(view.context)
        view.setOnTouchListener { _, _ -> KeyboardHelper.hideSoftInput(view) }
    }

    private fun setDropdown(context: Context) {
        filled_exposed_dropdown.setAdapter(
            ArrayAdapter(
                context,
                android.R.layout.simple_spinner_item,
                PRESET_NUMBER_OF_GAMES
            )
        )
    }

    override fun onNavigationItemSelected(item: MenuItem) =
        bottomNavigationHelper.onNavigationItemSelected(item, findNavController())
}
