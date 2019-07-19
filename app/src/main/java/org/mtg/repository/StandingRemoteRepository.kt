package org.mtg.repository

import org.mtg.api.StandingApi
import org.mtg.api.onApiErrorReturn
import timber.log.Timber

class StandingRemoteRepository(private val standingApi: StandingApi) {
    fun standingsForLeague(leagueId: Long) =
        standingApi.standingsForLeague(leagueId)
            .doOnError { Timber.w("matchResultsForLeague($leagueId) error: $it") }
            .onApiErrorReturn { emptyList() }
}
