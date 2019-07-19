package org.mtg.repository

import org.mtg.api.LeagueApi
import org.mtg.api.onApiErrorReturn
import timber.log.Timber

class LeagueRemoteRepository(private val leagueApi: LeagueApi) {
    fun leagues() =
        leagueApi.leagues()
            .doOnError { Timber.w("leagues() error: $it") }
            .onApiErrorReturn { emptyList() }

    fun matchResultsForLeague(leagueId: Long) =
        leagueApi.matchResultsForLeague(leagueId)
            .doOnError { Timber.w("matchResultsForLeague($leagueId) error: $it") }
            .onApiErrorReturn { emptyList() }
}
