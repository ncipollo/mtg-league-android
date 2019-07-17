package org.mtg.repository

import org.mtg.api.StandingApi

class StandingRemoteRepository(private val standingApi: StandingApi) {
    fun standingsForLeague(leagueId: Long) = standingApi.standingsForLeague(leagueId)
}
