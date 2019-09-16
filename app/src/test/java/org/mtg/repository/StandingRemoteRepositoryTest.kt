package org.mtg.repository

import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.api.StandingApi
import org.mtg.flow.sendSingle
import org.mtg.flow.testChannel
import org.mtg.flow.testCollect
import org.mtg.model.Standing
import java.io.IOException

@RunWith(JUnit4::class)
class StandingRemoteRepositoryTest {
    private companion object {
        const val LEAGUE_ID = 1L

        val ERROR = IOException()
        val STANDING = Standing(
            firstName = "Nick",
            lastName = "Cipollo",
            ranking = "1st",
            week1Record = "3-0",
            week2Record = "3-0",
            week3Record = "3-0",
            week4Record = "3-0"
        )
    }

    private val api = mock<StandingApi>()
    private val standingChannel = api.standingsForLeague(LEAGUE_ID).testChannel()

    private val repo = StandingRemoteRepository(api)

    @Test
    fun standing_error() = runBlockingTest {
        val collector = repo.standingsForLeague(LEAGUE_ID).testCollect(this)
        respondWithStandingError()
        collector.assertValues(emptyList()).assertNoError()
    }

    @Test
    fun standing_success() = runBlockingTest {
        val collector = repo.standingsForLeague(LEAGUE_ID).testCollect(this)
        respondWithStandings()
        collector.assertValues(listOf(STANDING)).assertNoError()
    }

    private suspend fun respondWithStandings() = standingChannel.sendSingle(listOf(STANDING))

    private fun respondWithStandingError() = standingChannel.close(ERROR)
}
