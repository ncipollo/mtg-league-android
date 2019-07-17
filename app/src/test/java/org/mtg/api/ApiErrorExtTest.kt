package org.mtg.api

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@RunWith(JUnit4::class)
class ApiErrorExtTest {
    private companion object {
        const val RECOVERY_VALUE = "recover"

        val HTTP_EXCEPTION = HttpException(Response.error<String>(404, ResponseBody.create(null, "")))
        val IO_EXCEPTION = IOException()
        val RUN_TIME_EXCEPTION = RuntimeException()
    }

    @Test
    fun completable_onApiErrorReturn_consumesHttpError() {
        val observer = Completable.error(HTTP_EXCEPTION)
            .onApiErrorComplete()
            .test()
        observer.assertComplete()
    }

    @Test
    fun completable_onApiErrorReturn_consumesIoError() {
        val observer = Completable.error(IO_EXCEPTION)
            .onApiErrorComplete()
            .test()
        observer.assertComplete()
    }

    @Test
    fun completable_onApiErrorReturn_throwsOtherErrors() {
        val observer = Completable.error(RUN_TIME_EXCEPTION)
            .onApiErrorComplete()
            .test()
        observer.assertError(RUN_TIME_EXCEPTION)
    }

    @Test
    fun observable_onApiErrorReturn_consumesHttpError() {
        val observer = Observable.error<String>(HTTP_EXCEPTION)
            .onApiErrorReturn { RECOVERY_VALUE }
            .test()
        observer.assertValue(RECOVERY_VALUE)
    }

    @Test
    fun observable_onApiErrorReturn_consumesIoError() {
        val observer = Observable.error<String>(IO_EXCEPTION)
            .onApiErrorReturn { RECOVERY_VALUE }
            .test()
        observer.assertValue(RECOVERY_VALUE)
    }

    @Test
    fun observable_onApiErrorReturn_throwsOtherErrors() {
        val observer = Observable.error<String>(RUN_TIME_EXCEPTION)
            .onApiErrorReturn { RECOVERY_VALUE }
            .test()

        observer.assertError(RuntimeException::class.java)
    }

    @Test
    fun single_onApiErrorReturn_consumesHttpError() {
        val observer = Single.error<String>(HTTP_EXCEPTION)
            .onApiErrorReturn { RECOVERY_VALUE }
            .test()
        observer.assertValue(RECOVERY_VALUE)
    }

    @Test
    fun single_onApiErrorReturn_consumesIoError() {
        val observer = Single.error<String>(IO_EXCEPTION)
            .onApiErrorReturn { RECOVERY_VALUE }
            .test()
        observer.assertValue(RECOVERY_VALUE)
    }

    @Test
    fun single_onApiErrorReturn_throwsOtherErrors() {
        val observer = Single.error<String>(RUN_TIME_EXCEPTION)
            .onApiErrorReturn { RECOVERY_VALUE }
            .test()

        observer.assertError(RuntimeException::class.java)
    }
}
