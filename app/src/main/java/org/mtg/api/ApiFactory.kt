package org.mtg.api

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class ApiFactory(private val context: Context) {
    companion object {
        const val BASE_URL = "https:///api.mtg-league.com/api/"
        const val CACHE_SIZE = 10 * 1024 * 1024
    }

    private val retrofit = createRetrofit()

    inline fun <reified T> createApi() = createApiFromClass(T::class.java)

    fun <T> createApiFromClass(service: Class<T>): T = retrofit.create(service)

    private fun createRetrofit(): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(createGson()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .baseUrl(BASE_URL)
            .client(createOkhttp())
            .build()

    private fun createGson() =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    private fun createOkhttp(): OkHttpClient {
        val cacheFile = File(context.cacheDir, "httpCache")
        val cache = Cache(cacheFile, CACHE_SIZE.toLong())
        return OkHttpClient.Builder()
            .cache(cache)
            .build()
    }
}
