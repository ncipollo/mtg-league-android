package org.mtg.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.mtg.api.LeagueApi
import org.mtg.model.League
import org.mtg.model.MatchResult
import java.io.IOException

class LeagueRemoteRepositoryTest {
    private companion object {
        const val LEAGUE_ID = 1L

        val ERROR = IOException()
        val LEAGUE = League(id = LEAGUE_ID, name = "M20")
        val MATCH_RESULT = MatchResult(gamesCount = 3)
    }

    private val leagueSubject = PublishSubject.create<List<League>>()
    private val matchSubject = PublishSubject.create<List<MatchResult>>()

    private val api = mock<LeagueApi> {
        on { leagues() } doReturn leagueSubject.firstOrError()
        on { matchResultsForLeague(LEAGUE_ID) } doReturn matchSubject.firstOrError()
    }
    private val repo = LeagueRemoteRepository(api)

    @Test
    fun leagues_error() {
        val observer = repo.leagues().test()
        respondWithLeagueError()
        observer.assertValue(emptyList())
    }

    @Test
    fun leagues_success() {
        val observer = repo.leagues().test()
        respondWithLeagues()
        observer.assertValue(listOf(LEAGUE))
    }

    @Test
    fun matchResultsForLeague_error() {
        val observer = repo.matchResultsForLeague(LEAGUE_ID).test()
        respondWithMatchResultError()
        observer.assertValue(emptyList())
    }

    @Test
    fun matchResultsForLeague_success() {
        val observer = repo.matchResultsForLeague(LEAGUE_ID).test()
        respondWithMatchResults()
        observer.assertValue(listOf(MATCH_RESULT))
    }

    private fun respondWithLeagues() = leagueSubject.onNext(listOf(LEAGUE))

    private fun respondWithLeagueError() = leagueSubject.onError(ERROR)

    private fun respondWithMatchResults() = matchSubject.onNext(listOf(MATCH_RESULT))

    private fun respondWithMatchResultError() = matchSubject.onError(ERROR)
}
