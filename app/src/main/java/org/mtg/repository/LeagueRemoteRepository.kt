package org.mtg.repository

import org.mtg.api.LeagueApi
import org.mtg.api.onApiErrorReturn

class LeagueRemoteRepository(private val leagueApi: LeagueApi) {
    fun leagues() =
        leagueApi.leagues()
            .onApiErrorReturn { emptyList() }

    fun matchResultsForLeague(leagueId: Long) =
        leagueApi.matchResultsForLeague(leagueId)
            .onApiErrorReturn { emptyList() }
}
