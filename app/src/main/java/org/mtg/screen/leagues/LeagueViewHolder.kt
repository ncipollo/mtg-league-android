package org.mtg.screen.leagues

import android.view.ViewGroup
import androidx.core.view.isGone
import kotlinx.android.synthetic.main.league_view_item.view.*
import org.mtg.R
import org.mtg.arch.ItemActionListener
import org.mtg.arch.ItemViewHolder
import org.mtg.arch.ItemViewState

class LeagueViewHolder(parent: ViewGroup, actionListener: ItemActionListener) :
    ItemViewHolder<LeagueItem>(parent, R.layout.league_view_item, actionListener) {
    private val name = itemView.league_name
    private val click = itemView.league_click
    private val divider = itemView.league_divider

    override fun onBindItem(item: LeagueItem) {
        click.setOnClickListener { sendActionForItem(item) }
        name.text = item.leagueName
        divider.isGone = !item.showDivider
    }
}

data class LeagueItem(
    val active: Boolean = false,
    val leagueId: Long = 0,
    val leagueName: String = "",
    val showDivider: Boolean = false
) : ItemViewState
