package org.mtg.api

import kotlinx.coroutines.flow.Flow
import org.mtg.model.League
import org.mtg.model.MatchResult
import retrofit2.http.GET
import retrofit2.http.Path

interface LeagueApi {
    @GET("leagues")
    fun leagues(): Flow<List<League>>

    @GET("leagues/{leagueId}/match_results")
    fun matchResultsForLeague(@Path("leagueId") leagueId: Long): Flow<List<MatchResult>>
}
