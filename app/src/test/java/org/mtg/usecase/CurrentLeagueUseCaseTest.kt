package org.mtg.usecase

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.model.League
import org.mtg.model.Settings
import org.mtg.repository.LeagueRemoteRepository
import org.mtg.repository.SettingsLocalRepository

@RunWith(JUnit4::class)
class CurrentLeagueUseCaseTest {
    private companion object {
        val SETTINGS_WITH_LEAGUE = Settings(selectedLeagueId = 1, selectedLeagueName = "M20")
        val SETTINGS_WITHOUT_LEAGUE = Settings()
        val LEAGUE1 = League(id = 2, name = "War of the Spark")
        val LEAGUE2 = League(id = 3, name = "M20")
    }

    private val leagueSubject = PublishSubject.create<List<League>>()
    private val settingsSubject = PublishSubject.create<Settings>()

    private val leagueRepo = mock<LeagueRemoteRepository> {
        on { leagues() } doReturn leagueSubject.firstOrError()
    }
    private val settingsRepo = mock<SettingsLocalRepository> {
        on { get() } doReturn settingsSubject
    }

    private val useCase = CurrentLeagueUseCase(leagueRepo = leagueRepo, settingsRepo = settingsRepo)

    @Test
    fun get_fromApi() {
        val observer = useCase.get().test()

        respondWithEmptySettings()
        respondWithRemoteLeague()

        observer.assertValues(
            CurrentLeagueUseCase.Result(
                leagueId = LEAGUE2.id,
                leagueName = LEAGUE2.name
            )
        )
    }

    @Test
    fun get_fromNowhere() {
        val observer = useCase.get().test()

        respondWithEmptySettings()
        respondWithRemoteError()

        observer.assertValues(
            CurrentLeagueUseCase.Result(
                leagueId = 0,
                leagueName = ""
            )
        )
    }

    @Test
    fun get_fromSettings() {
        val observer = useCase.get().test()

        respondWithSavedLeague()

        observer.assertValues(
            CurrentLeagueUseCase.Result(
                leagueId = SETTINGS_WITH_LEAGUE.selectedLeagueId,
                leagueName = SETTINGS_WITH_LEAGUE.selectedLeagueName
            )
        )
    }

    private fun respondWithEmptySettings() = settingsSubject.onNext(SETTINGS_WITHOUT_LEAGUE)

    private fun respondWithRemoteError() = leagueSubject.onNext(emptyList())

    private fun respondWithRemoteLeague() = leagueSubject.onNext(listOf(LEAGUE1, LEAGUE2))

    private fun respondWithSavedLeague() = settingsSubject.onNext(SETTINGS_WITH_LEAGUE)
}
