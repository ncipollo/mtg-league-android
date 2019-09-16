package org.mtg.domain

import io.reactivex.Single
import kotlinx.coroutines.rx2.asObservable
import org.mtg.model.League
import org.mtg.model.Settings
import org.mtg.repository.LeagueRemoteRepository
import org.mtg.repository.SettingsLocalRepository

class CurrentLeagueUseCase(
    private val leagueRepo: LeagueRemoteRepository,
    private val settingsRepo: SettingsLocalRepository
) {

    data class Result(val leagueId: Long, val leagueName: String)

    fun get() = settingsRepo.get().switchMapSingle { fetchIfNeeded(it) }

    private fun fetchIfNeeded(settings: Settings): Single<Result> {
        val id = settings.selectedLeagueId
        val name = settings.selectedLeagueName
        return if (validLeague(id)) {
            Single.just(Result(leagueId = id, leagueName = name))
        } else {
            leagueRepo.leagues()
                .asObservable()
                .map { resultFromLeague(it.lastOrNull() ?: League()) }
                .firstOrError()
        }
    }

    private fun validLeague(leagueId: Long) = leagueId > 0

    private fun resultFromLeague(league: League) =
        Result(leagueId = league.id, leagueName = league.name)
}
