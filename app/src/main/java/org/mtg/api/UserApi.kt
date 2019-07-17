package org.mtg.api

import io.reactivex.Completable
import io.reactivex.Single
import org.mtg.model.MatchResult
import org.mtg.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @GET("users")
    fun users(): Single<List<User>>

    @POST("users")
    fun createUser(@Body user: User): Completable

    @GET("users/{userId}/match_results")
    fun matchResultsForUser(@Path("userId") userId: Long): Single<List<MatchResult>>
}
