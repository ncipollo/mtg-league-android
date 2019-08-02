package org.mtg.screen.report

import io.reactivex.ObservableTransformer
import org.mtg.model.MatchResult
import org.mtg.model.User
import org.mtg.repository.MatchRemoteRepository
import org.mtg.repository.UserRemoteRepository

class ReportUseCase(
    private val matchRepo: MatchRemoteRepository,
    private val userRepo: UserRemoteRepository
) {
    sealed class Action {
        object Refresh : Action()
        data class ReportMatch(val matchResult: MatchResult) : Action()
    }

    sealed class Result {
        data class ReportError(val error: Throwable) : Result()
        object ReportInProgress : Result()
        object ReportSuccess : Result()
        data class RefreshComplete(val users: List<User>) : Result()
    }

    fun create() = ObservableTransformer<Action, Result> { source ->
        source.switchMap {
            when(it) {
                is Action.Refresh -> refresh()
                is Action.ReportMatch -> report(it.matchResult)
            }
        }
    }

    private fun report(matchResult: MatchResult) =
        matchRepo.reportMatch(matchResult)
            .map {
                when(it) {
                    is MatchRemoteRepository.Result.Error -> Result.ReportError(it.throwable)
                    is MatchRemoteRepository.Result.Success -> Result.ReportSuccess
                }
            }
            .startWith(Result.ReportInProgress)

    private fun refresh() =
        userRepo.users()
            .map { Result.RefreshComplete(it) }
            .toObservable()
}
