package org.mtg.screen.report

import com.jakewharton.rx.replayingShare
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import org.mtg.model.MatchResult
import org.mtg.model.User
import org.mtg.usecase.CurrentLeagueUseCase
import org.mtg.viewmodel.MagicViewModel


class ReportViewModel(
    private val currentLeagueUseCase: CurrentLeagueUseCase,
    private val reportUseCase: ReportUseCase
) : MagicViewModel() {
    private val events = PublishSubject.create<ReportViewEvent>()
    private val viewStateSource = createViewState()

    val viewState = viewStateSource.toLiveData()

    val snackText =
        viewStateSource.map { it.statusText }
            .filter { it.isNotEmpty() }
            .toSingleLiveEvent()

    fun sendEvent(event: ReportViewEvent) = events.onNext(event)

    private fun createViewState() =
        setup().switchMap { state ->
            events.switchMap { report(it, state) }
                .startWith(state)
        }
            .startWith(ReportViewState())
            .replayingShare()

    private fun setup() =
        Observables.combineLatest(currentLeague(), refresh()) { leagueResult, userResult ->
            ReportViewState(
                enableSubmit = true,
                leagueId = leagueResult.leagueId,
                users = userResult.users.items()
            )
        }

    private fun currentLeague() =
        currentLeagueUseCase.get()

    private fun refresh() =
        Observable.just(ReportUseCase.Action.Refresh)
            .compose(reportUseCase.create())
            .ofType<ReportUseCase.Result.RefreshComplete>()

    private fun List<User>.items() = map { UserItem(name = "${it.firstName} ${it.lastName}", user = it) }

    private fun report(event: ReportViewEvent, state: ReportViewState) =
        Observable.just(ReportUseCase.Action.ReportMatch(matchResult(event, state.leagueId)))
            .compose(reportUseCase.create())
            .filter { it !is ReportUseCase.Result.RefreshComplete }
            .map {
                when (it) {
                    is ReportUseCase.Result.ReportError -> errorViewState(it.error, state)
                    ReportUseCase.Result.ReportInProgress -> loadingViewState(state)
                    ReportUseCase.Result.ReportSuccess -> successViewState(state)
                    is ReportUseCase.Result.RefreshComplete -> throw IllegalStateException("$it wasn't filtered")
                }
            }

    private fun matchResult(event: ReportViewEvent, leagueId: Long) =
        MatchResult(
            winnerId = event.winnerId,
            loserId = event.loserId,
            leagueId = leagueId,
            gamesCount = event.gamesCount,
            userId = event.winnerId
        )

    private fun errorViewState(error: Throwable, state: ReportViewState) =
        state.copy(statusText = "Failed to report match!\nError: ${error.message}")

    private fun loadingViewState(state: ReportViewState) = state.copy(reporting = true)

    private fun successViewState(state: ReportViewState) = state.copy(statusText = "Reported Match! Sick!")
}

data class ReportViewEvent(val winnerId: Long, val loserId: Long, val gamesCount: Long = 2)

data class ReportViewState(
    val enableSubmit: Boolean = false,
    val leagueId: Long = 0,
    val reporting: Boolean = false,
    val statusText: String = "",
    val users: List<UserItem> = emptyList()
)

data class UserItem(
    val name: String = "",
    val user: User = User()
)
