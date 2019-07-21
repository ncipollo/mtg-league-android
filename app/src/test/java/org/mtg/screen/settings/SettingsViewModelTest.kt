package org.mtg.screen.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.model.Settings
import org.mtg.viewmodel.TestLifeCycleOwner
import org.mtg.viewmodel.TestObserver

@RunWith(JUnit4::class)
class SettingsViewModelTest {
    private companion object {
        val SETTINGS = Settings(darkMode = true, selectedLeagueName = "M20")
    }

    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    private val resultSubject = PublishSubject.create<SettingsUseCase.Result>()
    private val useCase = mock<SettingsUseCase> {
        on { create() } doReturn ObservableTransformer { source -> source.switchMap { resultSubject.take(2) } }
    }

    private val lifecycleOwner = TestLifeCycleOwner.started()
    private val observer = TestObserver<SettingsViewState>()
    private val viewModel = SettingsViewModel(useCase)

    @Test
    fun viewState_observe() {
        viewModel.viewState.observe(lifecycleOwner, observer)

        respondWithSettings()

        observer.assertValues(
            SettingsViewState(inProgress = true),
            SettingsViewState(settings = SETTINGS, inProgress = false)
        )
    }

    @Test
    fun viewState_observe_update() {
        viewModel.viewState.observe(lifecycleOwner, observer)

        respondWithSettings()
        viewModel.sendViewEvent(SettingsViewEvent.Update(SETTINGS))
        respondWithSaved()
        respondWithSettings()

        observer.assertValues(
            SettingsViewState(inProgress = true),
            SettingsViewState(settings = SETTINGS, inProgress = false),
            SettingsViewState(inProgress = true),
            SettingsViewState(settings = SETTINGS, inProgress = false)
        )
    }

    private fun respondWithSettings() {
        resultSubject.onNext(SettingsUseCase.Result.InProgress)
        resultSubject.onNext(SettingsUseCase.Result.Retrieved(SETTINGS))
    }

    private fun respondWithSaved() {
        resultSubject.onNext(SettingsUseCase.Result.InProgress)
        resultSubject.onNext(SettingsUseCase.Result.Saved)
    }
}
