package org.mtg.screen

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.mtg.R

abstract class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) =
        inflater.inflate(R.menu.main_menu, menu)


    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.settings_menu -> {
                findNavController().navigate(R.id.leaguesFragment)
                true
            }
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
