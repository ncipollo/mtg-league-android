package org.mtg.api

import io.reactivex.Completable
import io.reactivex.Single
import org.mtg.model.MatchResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MatchResultApi {
    @GET("match_results")
    fun matchResults(): Single<List<MatchResult>>

    @POST("match_results")
    fun reportMatch(@Body matchResult: MatchResult): Completable
}
