package org.mtg.screen.leagues

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.ObservableTransformer
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.flow.CoroutinesTestRule
import org.mtg.flow.testChannel
import org.mtg.model.League
import org.mtg.screen.settings.SettingsUseCase
import org.mtg.viewmodel.TestLifeCycleOwner
import org.mtg.viewmodel.TestObserver

@RunWith(JUnit4::class)
class LeaguesViewModelTest {
    private companion object {
        val LEAGUES = listOf(League(id = 0, name = "ixalan"))
        val ITEMS = listOf(LeagueItem(leagueId = 0, leagueName = "ixalan", showDivider = false))
    }

    @get:Rule
    val dispatcherRule = CoroutinesTestRule()
    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    private val itemFactory = mock<LeagueItemFactory> {
        on {items(LEAGUES)} doReturn ITEMS
    }
    private val leaguesUseCase = mock<LeaguesUseCase>()
    private val leaguesChannel = leaguesUseCase.get().testChannel()
    private val settingSubject = PublishSubject.create<SettingsUseCase.Result>()
    private val settingsUseCase = mock<SettingsUseCase> {
        on { create() } doReturn ObservableTransformer { source ->
            source.switchMap { settingSubject.take(2) }
        }
    }

    private val lifecycleOwner = TestLifeCycleOwner.started()
    private lateinit var viewModel: LeaguesViewModel

    @Before
    fun setUp() {
        viewModel = LeaguesViewModel(itemFactory, leaguesUseCase, settingsUseCase)
    }

    @Test
    fun navigationEvents() = runBlockingTest {
        val observer = TestObserver<LeaguesNavigationEvent>()
        viewModel.navigationEvents.observe(lifecycleOwner, observer)

        sendEvent()
        respondWithSettings()

        observer.assertValues(LeaguesNavigationEvent.Back)
    }

    @Test
    fun viewState() = runBlockingTest {
        val observer = TestObserver<LeaguesViewState>()
        viewModel.viewState.observe(lifecycleOwner, observer)

        respondWithLeagues()

        observer.assertValues(
            LeaguesViewState(loading = true),
            LeaguesViewState(items = ITEMS)
        )
    }

    private fun sendEvent() {
        val league = LEAGUES[0]
        viewModel.sendViewEvent(LeaguesViewEvent.Selected(league.id, league.name))
    }

    private suspend fun respondWithLeagues() {
        leaguesChannel.send(LeaguesUseCase.Result.InProgress)
        leaguesChannel.send(LeaguesUseCase.Result.Success(LEAGUES))
        leaguesChannel.close()
    }


    private fun respondWithSettings() =
        settingSubject.onNext(SettingsUseCase.Result.Saved)


}
