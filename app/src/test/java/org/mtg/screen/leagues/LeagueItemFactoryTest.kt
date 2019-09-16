package org.mtg.screen.leagues

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.model.League
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class LeagueItemFactoryTest {
    private companion object {
        val LEAGUES = listOf(1L, 2L, 3L)
            .map { League(id = it, name = "$it", active = it == 1L) }
    }

    @Test
    fun items() {
        val items = LeagueItemFactory().items(LEAGUES)

        val expected = listOf(
            LeagueItem(active = false, leagueId = 3, leagueName = "3", showDivider = true),
            LeagueItem(active = false, leagueId = 2, leagueName = "2", showDivider = true),
            LeagueItem(active = true, leagueId = 1, leagueName = "1", showDivider = false)
        )
        assertEquals(expected = expected, actual = items)
    }
}
