package org.mtg.repository

import org.mtg.api.LeagueApi
import org.mtg.api.onApiErrorReturn
import org.mtg.flow.onError
import timber.log.Timber

class LeagueRemoteRepository(private val leagueApi: LeagueApi) {
    fun leagues() =
        leagueApi.leagues()
            .onError { Timber.w("leagues() error: $it") }
            .onApiErrorReturn { emptyList() }

    fun matchResultsForLeague(leagueId: Long) =
        leagueApi.matchResultsForLeague(leagueId)
            .onError { Timber.w("matchResultsForLeague($leagueId) error: $it") }
            .onApiErrorReturn { emptyList() }
}
