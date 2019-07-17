package org.mtg.api

import io.reactivex.Single
import org.mtg.model.Standing
import retrofit2.http.GET
import retrofit2.http.Path

interface StandingApi {
    @GET("leagues/{leagueId}/standings")
    fun standingForLeague(@Path("leagueId") leagueId: Long) : Single<List<Standing>>
}
