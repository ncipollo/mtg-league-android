package org.mtg.api

import io.reactivex.Completable
import io.reactivex.Single
import org.mtg.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    @GET("usersx")
    fun users(): Single<List<User>>

    @POST("users")
    fun createUser(@Body user: User): Completable
}
