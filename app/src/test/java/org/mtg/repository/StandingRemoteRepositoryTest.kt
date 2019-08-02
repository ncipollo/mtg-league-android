package org.mtg.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.api.StandingApi
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

    private val standingSubject = PublishSubject.create<List<Standing>>()

    private val api = mock<StandingApi> {
        on { standingsForLeague(LEAGUE_ID) } doReturn standingSubject.firstOrError()
    }
    private val repo = StandingRemoteRepository(api)

    @Test
    fun standing_error() {
        val observer = repo.standingsForLeague(LEAGUE_ID).test()
        respondWithStandingError()
        observer.assertValue(emptyList())
    }

    @Test
    fun standing_success() {
        val observer = repo.standingsForLeague(LEAGUE_ID).test()
        respondWithStandings()
        observer.assertValue(listOf(STANDING))
    }

    private fun respondWithStandings() = standingSubject.onNext(listOf(STANDING))

    private fun respondWithStandingError() = standingSubject.onError(ERROR)
}
