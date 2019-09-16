package org.mtg.screen.report

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.model.User
import org.mtg.domain.CurrentLeagueUseCase
import org.mtg.viewmodel.TestLifeCycleOwner
import org.mtg.viewmodel.TestObserver
import java.io.IOException

@RunWith(JUnit4::class)
class ReportViewModelTest {
    private companion object {
        const val GAMES = 3L
        const val LEAGUE_ID = 1L
        const val LEAGUE_NAME = "M20"

        val ERROR = IOException("bad stuff")
        val LOSER = User(id = 2, firstName = "Someone", lastName = "Else")
        val WINNER = User(id = 1, firstName = "Nick", lastName = "Cipollo")
    }

    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    private val leagueSubject = PublishSubject.create<CurrentLeagueUseCase.Result>()
    private val leagueUseCase = mock<CurrentLeagueUseCase> {
        on { get() } doReturn leagueSubject
    }
    private val reportSubject = PublishSubject.create<ReportUseCase.Result>()
    private val reportUseCase = mock<ReportUseCase> {
        on { create() } doReturn ObservableTransformer { source -> source.switchMap { reportSubject.take(2) } }
    }

    private val lifecycleOwner = TestLifeCycleOwner.started()
    private val snackStateObserver = TestObserver<String>()
    private val viewStateObserver = TestObserver<ReportViewState>()
    private val viewModel = ReportViewModel(leagueUseCase, reportUseCase)

    @Test
    fun sendEvent_reportFailed() {
        viewModel.snackText.observe(lifecycleOwner, snackStateObserver)
        viewModel.viewState.observe(lifecycleOwner, viewStateObserver)

        respondWithCurrentLeague()
        respondWithRefreshComplete()
        sendReport()
        respondWithReportError()

        snackStateObserver.assertValues("Failed to report match!\nError: ${ERROR.message}")
        viewStateObserver.assertValues(
            ReportViewState(),
            ReportViewState(
                enableSubmit = true,
                leagueId = LEAGUE_ID,
                users = listOf(LOSER, WINNER).items()
            ),
            ReportViewState(
                enableSubmit = true,
                leagueId = LEAGUE_ID,
                reporting = true,
                users = listOf(LOSER, WINNER).items()
            ),
            ReportViewState(
                enableSubmit = true,
                leagueId = LEAGUE_ID,
                statusText = "Failed to report match!\nError: ${ERROR.message}",
                users = listOf(LOSER, WINNER).items()
            )
        )
    }

    @Test
    fun sendEvent_reportSuccessful() {
        viewModel.snackText.observe(lifecycleOwner, snackStateObserver)
        viewModel.viewState.observe(lifecycleOwner, viewStateObserver)

        respondWithCurrentLeague()
        respondWithRefreshComplete()
        sendReport()
        respondWithReportSuccess()

        snackStateObserver.assertValues("Reported Match! Sick!")
        viewStateObserver.assertValues(
            ReportViewState(),
            ReportViewState(
                enableSubmit = true,
                leagueId = LEAGUE_ID,
                users = listOf(LOSER, WINNER).items()
            ),
            ReportViewState(
                enableSubmit = true,
                leagueId = LEAGUE_ID,
                reporting = true,
                users = listOf(LOSER, WINNER).items()
            ),
            ReportViewState(
                enableSubmit = true,
                leagueId = LEAGUE_ID,
                statusText = "Reported Match! Sick!",
                users = listOf(LOSER, WINNER).items()
            )
        )
    }

    @Test
    fun viewState() {
        viewModel.snackText.observe(lifecycleOwner, snackStateObserver)
        viewModel.viewState.observe(lifecycleOwner, viewStateObserver)

        respondWithCurrentLeague()
        respondWithRefreshComplete()

        snackStateObserver.assertValues()
        viewStateObserver.assertValues(
            ReportViewState(),
            ReportViewState(
                enableSubmit = true,
                leagueId = LEAGUE_ID,
                users = listOf(LOSER, WINNER).items()
            )
        )
    }

    private fun respondWithCurrentLeague() =
        leagueSubject.onNext(CurrentLeagueUseCase.Result(LEAGUE_ID, LEAGUE_NAME))

    private fun respondWithRefreshComplete() =
        reportSubject.onNext(ReportUseCase.Result.RefreshComplete(listOf(LOSER, WINNER)))

    private fun respondWithReportError() =
        reportSubject.apply {
            onNext(ReportUseCase.Result.ReportInProgress)
            onNext(ReportUseCase.Result.ReportError(ERROR))
        }

    private fun respondWithReportSuccess() =
        reportSubject.apply {
            onNext(ReportUseCase.Result.ReportInProgress)
            onNext(ReportUseCase.Result.ReportSuccess)
        }

    private fun sendReport() =
        viewModel.sendEvent(
            ReportViewEvent(
                winnerId = WINNER.id,
                loserId = LOSER.id,
                gamesCount = GAMES
            )
        )

    private fun List<User>.items() = map { UserItem(name = "${it.firstName} ${it.lastName}", user = it) }
}
