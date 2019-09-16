package org.mtg.repository

import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.api.LeagueApi
import org.mtg.flow.sendSingle
import org.mtg.flow.testChannel
import org.mtg.flow.testCollect
import org.mtg.model.League
import org.mtg.model.MatchResult
import java.io.IOException

@RunWith(JUnit4::class)
class LeagueRemoteRepositoryTest {
    private companion object {
        const val LEAGUE_ID = 1L

        val ERROR = IOException()
        val LEAGUE = League(id = LEAGUE_ID, name = "M20")
        val MATCH_RESULT = MatchResult(gamesCount = 3)
    }

    private val api = mock<LeagueApi>()
    private val leagueChannel = api.leagues().testChannel()
    private val matchChannel = api.matchResultsForLeague(LEAGUE_ID).testChannel()
    private val repo = LeagueRemoteRepository(api)

    @Test
    fun leagues_error() = runBlockingTest {
        val collector = repo.leagues().testCollect(this)
        respondWithLeagueError()
        collector.assertValues(emptyList()).assertNoError()
    }

    @Test
    fun leagues_success() = runBlockingTest {
        val collector = repo.leagues().testCollect(this)
        respondWithLeagues()
        collector.assertValues(listOf(LEAGUE)).assertNoError()
    }

    @Test
    fun matchResultsForLeague_error() = runBlockingTest {
        val collector = repo.matchResultsForLeague(LEAGUE_ID).testCollect(this)
        respondWithMatchResultError()
        collector.assertValues(emptyList()).assertNoError()
    }

    @Test
    fun matchResultsForLeague_success() = runBlockingTest {
        val collector = repo.matchResultsForLeague(LEAGUE_ID).testCollect(this)
        respondWithMatchResults()
        collector.assertValues(listOf(MATCH_RESULT)).assertNoError()
    }

    private suspend fun respondWithLeagues() = leagueChannel.sendSingle(listOf(LEAGUE))

    private fun respondWithLeagueError() = leagueChannel.close(ERROR)

    private suspend fun respondWithMatchResults() = matchChannel.sendSingle(listOf(MATCH_RESULT))

    private fun respondWithMatchResultError() = matchChannel.close(ERROR)
}
