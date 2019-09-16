package org.mtg.repository

import org.mtg.api.StandingApi
import org.mtg.api.onApiErrorReturn
import org.mtg.flow.onError
import timber.log.Timber

class StandingRemoteRepository(private val standingApi: StandingApi) {
    fun standingsForLeague(leagueId: Long) =
        standingApi.standingsForLeague(leagueId)
            .onError { Timber.w("matchResultsForLeague($leagueId) error: $it") }
            .onApiErrorReturn { emptyList() }
}
