package org.mtg.api

import kotlinx.coroutines.flow.Flow
import org.mtg.model.Standing
import retrofit2.http.GET
import retrofit2.http.Path

interface StandingApi {
    @GET("leagues/{leagueId}/standings")
    fun standingsForLeague(@Path("leagueId") leagueId: Long) : Flow<List<Standing>>
}
