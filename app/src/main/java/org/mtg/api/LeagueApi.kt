package org.mtg.api

import io.reactivex.Single
import org.mtg.model.League
import org.mtg.model.MatchResult
import retrofit2.http.GET
import retrofit2.http.Path

interface LeagueApi {
    @GET("leagues")
    fun leagues(): Single<List<League>>

    @GET("leagues/{leagueId}/match_results")
    fun matchResultsForLeague(@Path("leagueId") leagueId: Long): Single<List<MatchResult>>
}
