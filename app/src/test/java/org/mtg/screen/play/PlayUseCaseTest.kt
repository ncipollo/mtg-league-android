package org.mtg.screen.play

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mtg.flow.TestCollector
import org.mtg.flow.testCollect
import org.mtg.model.Player
import org.mtg.model.Score
import org.mtg.model.ScoreBoard

class PlayUseCaseTest {

    private val channel = BroadcastChannel<PlayUseCase.Action>(Channel.BUFFERED)
    private val upstream = channel.asFlow()

    private val useCase = PlayUseCase()
    private val flow = upstream.let { useCase.get(it) }

    @Test
    fun get_scoreBoardIsCached() = runBlockingTest {
        flow.testCollect(this)
        decrementBottom()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)
        channel.close()

        val secondChannel =  BroadcastChannel<PlayUseCase.Action>(Channel.BUFFERED)
        val secondUpstream = secondChannel.asFlow()
        val collector = secondUpstream.let { useCase.get(it) }.testCollect(this)
        val player = Player() - 1
        collector.assertScoreBoards(
            ScoreBoard(bottomPlayer = player.copy(score = player.score.copy(lifeChange = 0)))
        )

        secondChannel.close()
    }

    @Test
    fun get_decrementBottom() = runBlockingTest {
        val collector = flow.testCollect(this)

        decrementBottom()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)

        val player = Player() - 1
        collector.assertScoreBoards(
            ScoreBoard(),
            ScoreBoard(bottomPlayer = player),
            ScoreBoard(bottomPlayer = player.copy(score = player.score.copy(lifeChange = 0)))
        )
        channel.close()
    }

    @Test
    fun get_decrementBottom_clearOnlySendAfterDelay() = runBlockingTest {
        val collector = flow.testCollect(this)

        decrementBottom()

        val player = Player() - 1
        collector.assertScoreBoards(
            ScoreBoard(),
            ScoreBoard(bottomPlayer = player)
        )
        channel.close()
    }

    @Test
    fun get_decrementBottom_multipleEventsSent() = runBlockingTest {
        val collector = flow.testCollect(this)

        decrementBottom()
        decrementBottom()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN * 2)
        decrementBottom()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)

        val player1 = Player() - 1
        val player2 = Player() - 2
        val player3 = Player() - 3
        collector.assertScoreBoards(
            ScoreBoard(),
            ScoreBoard(bottomPlayer = player1),
            ScoreBoard(bottomPlayer = player2),
            ScoreBoard(bottomPlayer = player2.copy(score = player2.score.copy(lifeChange = 0))),
            ScoreBoard(bottomPlayer = player3.copy(score = player3.score.copy(lifeChange = -1))),
            ScoreBoard(bottomPlayer = player3.copy(score = player3.score.copy(lifeChange = 0)))
        )
        channel.close()
    }

    @Test
    fun get_decrementTop() = runBlockingTest {
        val collector = flow.testCollect(this)

        decrementTop()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)

        val player = Player() - 1
        collector.assertScoreBoards(
            ScoreBoard(),
            ScoreBoard(topPlayer = player),
            ScoreBoard(topPlayer = player.copy(score = player.score.copy(lifeChange = 0)))
        )
        channel.close()
    }

    @Test
    fun get_emitsInitialScoreBoard() = runBlockingTest {
        val collector = flow.testCollect(this)

        collector.assertValues(PlayUseCase.Result(ScoreBoard()))
        channel.close()
    }

    @Test
    fun get_incrementBottom() = runBlockingTest {
        val collector = flow.testCollect(this)

        incrementBottom()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)

        val player = Player() + 1
        collector.assertScoreBoards(
            ScoreBoard(),
            ScoreBoard(bottomPlayer = player),
            ScoreBoard(bottomPlayer = player.copy(score = player.score.copy(lifeChange = 0)))
        )
        channel.close()
    }

    @Test
    fun get_incrementTop() = runBlockingTest {
        val collector = flow.testCollect(this)

        incrementTop()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)

        val player = Player() + 1
        collector.assertScoreBoards(
            ScoreBoard(),
            ScoreBoard(topPlayer = player),
            ScoreBoard(topPlayer = player.copy(score = player.score.copy(lifeChange = 0)))
        )
        channel.close()
    }

    @Test
    fun get_reset() = runBlockingTest {
        val collector = flow.testCollect(this)

        incrementTop()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)
        reset()
        advanceTimeBy(PlayUseCase.LIFE_CHANGE_LIFE_SPAN)

        val player = Player() + 1
        val resetPlayer = Player().copy(score = Score().copy(life = 20))
        collector.assertScoreBoards(
            ScoreBoard(),
            ScoreBoard(topPlayer = player),
            ScoreBoard(topPlayer = player.copy(score = player.score.copy(lifeChange = 0))),
            ScoreBoard(topPlayer = resetPlayer, bottomPlayer = resetPlayer)
        )
        channel.close()
    }

    private fun TestCollector<PlayUseCase.Result>.assertScoreBoards(vararg boards: ScoreBoard) {
        val results = boards.map { PlayUseCase.Result(it) }
        assertValues(*results.toTypedArray())
        assertNoError()
    }

    private suspend fun decrementBottom() = channel.send(PlayUseCase.Action.DecrementBottomPlayer)

    private suspend fun decrementTop() = channel.send(PlayUseCase.Action.DecrementTopPlayer)

    private suspend fun incrementBottom() = channel.send(PlayUseCase.Action.IncrementBottomPlayer)

    private suspend fun incrementTop() = channel.send(PlayUseCase.Action.IncrementTopPlayer)

    private suspend fun reset() = channel.send(PlayUseCase.Action.Reset())
}
