package org.mtg.screen.leagues

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.mtg.model.League
import org.mtg.repository.LeagueRemoteRepository

class LeaguesUseCase(private val leaguesRepo: LeagueRemoteRepository) {
    sealed class Result {
        object InProgress : Result()
        data class Success(val leagues: List<League>) : Result()
    }

    fun get() =
        leaguesRepo.leagues()
            .map { Result.Success(it) }
            .onStart<Result> { emit(Result.InProgress) }
}
