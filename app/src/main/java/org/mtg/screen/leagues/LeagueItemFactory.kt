package org.mtg.screen.leagues

import org.mtg.model.League

class LeagueItemFactory {
    fun items(leagues: List<League>) =
        leagues.reversed().mapIndexed { index, league ->
            LeagueItem(
                active = league.active,
                leagueId = league.id,
                leagueName = league.name,
                showDivider = index < leagues.lastIndex
            )
        }
}
