package org.mtg.api

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.HttpException
import java.io.IOException

private typealias ApiResumeFunction<T> = (Throwable) -> T

fun Completable.onApiErrorComplete() =
    this.onErrorComplete { it.isApiError() }

fun <T> Observable<T>.onApiErrorReturn(resumeFunction: ApiResumeFunction<T>): Observable<T> =
    this.onErrorReturn { checkForApiError(it, resumeFunction) }

fun <T> Single<T>.onApiErrorReturn(resumeFunction: ApiResumeFunction<T>) =
    this.onErrorReturn { checkForApiError(it, resumeFunction) }

private fun <T> checkForApiError(error: Throwable, resumeFunction: ApiResumeFunction<T>): T {
    if (error.isApiError()) {
        return resumeFunction(error)
    }
    throw error
}

private fun Throwable.isApiError() = this is HttpException || this is IOException
