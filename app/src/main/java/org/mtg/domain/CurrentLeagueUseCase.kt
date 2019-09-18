package org.mtg.domain

import io.reactivex.Single
import kotlinx.coroutines.rx2.asObservable
import org.mtg.model.League
import org.mtg.model.Settings
import org.mtg.repository.LeagueRemoteRepository
import org.mtg.repository.SettingsLocalRepository

class CurrentLeagueUseCase(
    leagueRepo: LeagueRemoteRepository,
    settingsRepo: SettingsLocalRepository
) {

    data class Result(val leagueId: Long, val leagueName: String)

    private val results = settingsRepo.get().switchMapSingle { fetchIfNeeded(it) }
    private val remoteLeague =
        leagueRepo.leagues()
            .asObservable()
            .map { resultFromLeague(it.lastOrNull() ?: League()) }
            .firstOrError()
            .cache()

    fun get() = results

    private fun fetchIfNeeded(settings: Settings): Single<Result> {
        val id = settings.selectedLeagueId
        val name = settings.selectedLeagueName
        return if (validLeague(id)) {
            Single.just(Result(leagueId = id, leagueName = name))
        } else {
            remoteLeague
        }
    }

    private fun validLeague(leagueId: Long) = leagueId > 0

    private fun resultFromLeague(league: League) =
        Result(leagueId = league.id, leagueName = league.name)
}
