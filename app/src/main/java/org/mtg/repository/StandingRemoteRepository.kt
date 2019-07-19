package org.mtg.repository

import org.mtg.api.StandingApi
import org.mtg.api.onApiErrorReturn

class StandingRemoteRepository(private val standingApi: StandingApi) {
    fun standingsForLeague(leagueId: Long) =
        standingApi.standingsForLeague(leagueId)
            .onApiErrorReturn { emptyList() }
}
