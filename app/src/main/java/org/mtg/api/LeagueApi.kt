package org.mtg.api

import io.reactivex.Single
import org.mtg.model.League
import retrofit2.http.GET

interface LeagueApi {
    @GET("leagues")
    fun leagues(): Single<List<League>>
}
