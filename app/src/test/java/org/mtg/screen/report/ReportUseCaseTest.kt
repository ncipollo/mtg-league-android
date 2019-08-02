package org.mtg.screen.report

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mtg.model.MatchResult
import org.mtg.model.User
import org.mtg.repository.MatchRemoteRepository
import org.mtg.repository.UserRemoteRepository
import java.io.IOException

class ReportUseCaseTest {
    private companion object {
        val ERROR = IOException()
        val MATCH_RESULT = MatchResult(
            id = 0,
            winnerId = 1,
            loserId = 2,
            gamesCount = 3
        )
        val USER = User(
            id = 1,
            firstName = "Nick",
            lastName = "Cipollo",
            email = "nope@email.com"
        )
    }

    private val actionSubject = PublishSubject.create<ReportUseCase.Action>()
    private val matchReportSubject = PublishSubject.create<MatchRemoteRepository.Result>()
    private val matchRepo = mock<MatchRemoteRepository> {
        on { reportMatch(MATCH_RESULT) } doReturn matchReportSubject
    }
    private val userSubject = PublishSubject.create<List<User>>()
    private val userRepo = mock<UserRemoteRepository> {
        on { users() } doReturn userSubject.firstOrError()
    }
    private val useCase = ReportUseCase(matchRepo, userRepo)


    @Test
    fun create_fetchUsers() {
        val observer = actionSubject.compose(useCase.create()).test()

        sendRefresh()
        respondWithUsers()

        observer.assertValue(ReportUseCase.Result.RefreshComplete(listOf(USER)))
    }

    @Test
    fun create_reportError() {
        val observer = actionSubject.compose(useCase.create()).test()

        sendReport()
        respondWithReportError()

        observer.assertValues(
            ReportUseCase.Result.ReportInProgress,
            ReportUseCase.Result.ReportError(ERROR)
        )
    }

    @Test
    fun create_reportSuccess() {
        val observer = actionSubject.compose(useCase.create()).test()

        sendReport()
        respondWithReportSuccess()

        observer.assertValues(
            ReportUseCase.Result.ReportInProgress,
            ReportUseCase.Result.ReportSuccess
        )
    }

    private fun respondWithUsers() = userSubject.onNext(listOf(USER))

    private fun respondWithReportError() = matchReportSubject.onNext(MatchRemoteRepository.Result.Error(ERROR))

    private fun respondWithReportSuccess() = matchReportSubject.onNext(MatchRemoteRepository.Result.Success)

    private fun sendRefresh() = actionSubject.onNext(ReportUseCase.Action.Refresh)

    private fun sendReport() = actionSubject.onNext(ReportUseCase.Action.ReportMatch(MATCH_RESULT))
}
