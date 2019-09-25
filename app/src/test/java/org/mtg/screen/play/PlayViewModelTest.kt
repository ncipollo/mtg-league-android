package org.mtg.screen.play

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mtg.flow.CoroutinesTestRule
import org.mtg.flow.testChannel
import org.mtg.model.Player
import org.mtg.model.Score
import org.mtg.model.ScoreBoard
import org.mtg.viewmodel.TestLifeCycleOwner
import org.mtg.viewmodel.TestObserver

class PlayViewModelTest {

    private companion object {
        val BOTTOM_PLAYER = Player(score = Score(life = 1))
        val BOTTOM_SCORE = ScoreViewState(scoreText = "1")
        val TOP_PLAYER = Player(score = Score(life = 2))
        val TOP_SCORE = ScoreViewState(scoreText = "2")
    }

    @get:Rule
    val dispatcherRule = CoroutinesTestRule()
    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    private val scoreFactory = mock<ScoreViewStateFactory> {
        on { fromPlayer(BOTTOM_PLAYER) } doReturn BOTTOM_SCORE
        on { fromPlayer(TOP_PLAYER) } doReturn TOP_SCORE
    }
    private val playUseCase = mock<PlayUseCase>()
    private val playChannel = playUseCase.get(any()).testChannel()

    private val lifecycleOwner = TestLifeCycleOwner.started()
    private lateinit var viewModel: PlayViewModel

    @Before
    fun setUp() {
        viewModel = PlayViewModel(playUseCase, scoreFactory)
    }

    @Test
    fun viewState() = runBlockingTest {
        val observer = TestObserver<PlayViewState>()
        viewModel.viewState.observe(lifecycleOwner, observer)

        respondWithScoreboard()

        observer.assertValues(
            PlayViewState(BOTTOM_SCORE, TOP_SCORE)
        )
    }

    @Test
    fun viewState_sendEvent() = runBlockingTest {
        val observer = TestObserver<PlayViewState>()
        viewModel.viewState.observe(lifecycleOwner, observer)

        respondWithScoreboard()
        viewModel.sendViewEvent(PlayViewEvent.IncrementTopPlayer)
        respondWithScoreboard()

        observer.assertValues(
            PlayViewState(BOTTOM_SCORE, TOP_SCORE),
            PlayViewState(BOTTOM_SCORE, TOP_SCORE)
        )
    }

    private suspend fun respondWithScoreboard() {
        playChannel.send(PlayUseCase.Result(ScoreBoard(BOTTOM_PLAYER, TOP_PLAYER)))
    }
}
