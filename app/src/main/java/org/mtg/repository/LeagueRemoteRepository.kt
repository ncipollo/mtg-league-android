package org.mtg.repository

import org.mtg.api.LeagueApi

class LeagueRemoteRepository(private val leagueApi: LeagueApi) {
    fun leagues() = leagueApi.leagues()

    fun matchResultsForLeague(leagueId: Long) = leagueApi.matchResultsForLeague(leagueId)
}
