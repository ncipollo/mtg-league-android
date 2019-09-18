package org.mtg.screen.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.bottom_navigation.*
import org.koin.android.ext.android.inject
import org.mtg.R
import org.mtg.screen.HomeFragment
import org.mtg.util.BottomNavigationHelper

class ScoreboardFragment : HomeFragment(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_scoreboard, container, false)

    private val bottomNavigationHelper: BottomNavigationHelper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottom_navigation.selectedItemId = R.id.navigation_play

        bottomNavigationHelper.setupBottomNavigationTheme(view.context, bottom_navigation)
        bottom_navigation.setOnNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem) =
        bottomNavigationHelper.onNavigationItemSelected(item, findNavController())
}
