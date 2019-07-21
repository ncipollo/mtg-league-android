package org.mtg.screen.settings

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
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

        observer.assertValues(SettingsUseCase.Result.InProgress, SettingsUseCase.Result.Retrieved(SETTINGS))
    }

    @Test
    fun create_save() {
        val observer = source.compose(useCase.create()).test()

        sendSave()

        observer.assertValues(SettingsUseCase.Result.InProgress, SettingsUseCase.Result.Saved)
    }

    private fun sendFetch() = source.onNext(SettingsUseCase.Action.Fetch)

    private fun sendSave() = source.onNext(SettingsUseCase.Action.Save(SETTINGS))

    private fun respondWithSettings() = fetchSubject.onNext(SETTINGS)
}
