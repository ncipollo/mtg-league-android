package org.mtg.screen.leagues

import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.flow.sendSingle
import org.mtg.flow.testChannel
import org.mtg.flow.testCollect
import org.mtg.model.League
import org.mtg.repository.LeagueRemoteRepository

@RunWith(JUnit4::class)
class LeaguesUseCaseTest {
    private companion object {
        private val LEAGUES = listOf(League(id = 1, name = "thrones"))
    }

    private val leaguesRepo = mock<LeagueRemoteRepository>()
    private val leaguesChannel = leaguesRepo.leagues().testChannel()
    private val useCase = LeaguesUseCase(leaguesRepo)

    @Test
    fun get() = runBlockingTest {
        val collector = useCase.get().testCollect(this)

        leaguesChannel.sendSingle(LEAGUES)

        collector.assertValues(
            LeaguesUseCase.Result.InProgress,
            LeaguesUseCase.Result.Success(LEAGUES)
        )
        collector.assertNoError()
    }
}
