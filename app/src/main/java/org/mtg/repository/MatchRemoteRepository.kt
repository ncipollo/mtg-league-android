package org.mtg.repository

import io.reactivex.Observable
import org.mtg.api.MatchResultApi
import org.mtg.api.onApiErrorReturn
import org.mtg.model.MatchResult
import timber.log.Timber

class MatchRemoteRepository(private val matchApi: MatchResultApi) {
    sealed class Result {
        data class Error(val throwable: Throwable) : Result()
        object Success: Result()
    }
    fun reportMatch(matchResult: MatchResult) =
        matchApi.reportMatch(matchResult)
            .doOnError { Timber.w("reportMatch $matchResult error: $it") }
            .andThen(Observable.just<Result>(Result.Success))
            .onApiErrorReturn { Result.Error(it) }
}
