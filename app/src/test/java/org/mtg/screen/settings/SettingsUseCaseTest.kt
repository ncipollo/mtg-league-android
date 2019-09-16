package org.mtg.screen.settings

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.subjects.PublishSubject
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.model.Settings
import org.mtg.repository.SettingsLocalRepository

@RunWith(JUnit4::class)
class SettingsUseCaseTest {
    private companion object {
        const val LEAGUE_ID = 1L
        val SETTINGS = Settings(darkMode = true)
    }

    private val fetchSubject = PublishSubject.create<Settings>()

    private val repo = mock<SettingsLocalRepository> {
        on { get() } doReturn fetchSubject
        on { update(SETTINGS) } doReturn Completable.complete()
    }
    private val useCase = SettingsUseCase(repo)
    private val source = PublishSubject.create<SettingsUseCase.Action>()

    @Test
    fun create_fetch() {
        val observer = source.compose(useCase.create()).test()

        sendFetch()
        respondWithSettings()

        observer.assertValues(
            SettingsUseCase.Result.InProgress,
            SettingsUseCase.Result.Retrieved(SETTINGS)
        )
    }

    @Test
    fun create_save() {
        val observer = source.compose(useCase.create()).test()

        sendSave()

        observer.assertValues(SettingsUseCase.Result.InProgress, SettingsUseCase.Result.Saved)
    }

    @Test
    fun create_update() {
        val observer = source.compose(useCase.create()).test()
        whenever(repo.update(SETTINGS.copy(selectedLeagueId = LEAGUE_ID)))
            .thenReturn(Completable.complete())

        sendUpdate()
        respondWithSettings()

        observer.assertValues(SettingsUseCase.Result.InProgress, SettingsUseCase.Result.Saved)
    }

    private fun sendFetch() = source.onNext(SettingsUseCase.Action.Fetch)

    private fun sendSave() = source.onNext(SettingsUseCase.Action.Save(SETTINGS))

    private fun sendUpdate() =
        source.onNext(SettingsUseCase.Action.Update(selectedLeagueId = LEAGUE_ID))

    private fun respondWithSettings() = fetchSubject.onNext(SETTINGS)
}
