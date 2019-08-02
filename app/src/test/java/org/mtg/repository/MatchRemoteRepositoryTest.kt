package org.mtg.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.subjects.CompletableSubject
import org.junit.Test
import org.mtg.api.MatchResultApi
import org.mtg.model.MatchResult
import java.io.IOException

class MatchRemoteRepositoryTest {

    private companion object {
        val ERROR = IOException()
        val MATCH_RESULT = MatchResult(
            id = 0,
            winnerId = 1,
            loserId = 2,
            gamesCount = 3
        )
    }

    private val reportSubject = CompletableSubject.create()
    private val api = mock<MatchResultApi> {
        on { reportMatch(MATCH_RESULT) } doReturn reportSubject
    }
    private val repo = MatchRemoteRepository(api)

    @Test
    fun reportMatch_error() {
        val observer = repo.reportMatch(MATCH_RESULT).test()
        respondWithReportError()
        observer.assertValue(MatchRemoteRepository.Result.Error(ERROR))
    }

    @Test
    fun reportMatch_success() {
        val observer = repo.reportMatch(MATCH_RESULT).test()
        respondWithReportSuccess()
        observer.assertValue(MatchRemoteRepository.Result.Success)
    }

    private fun respondWithReportError() = reportSubject.onError(ERROR)

    private fun respondWithReportSuccess() = reportSubject.onComplete()
}
