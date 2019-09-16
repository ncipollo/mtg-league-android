package org.mtg.flow

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(JUnit4::class)
class FlowExtTest {
    private val channel = BroadcastChannel<String>(Channel.BUFFERED)

    @Test
    fun onError_calledWhenErrorIsEmitted() = runBlockingTest {
        var receivedError = false

        channel.asFlow().onError { receivedError = true }.testCollect(this)
        channel.close(RuntimeException("error!"))

        assertTrue(receivedError)
    }

    @Test
    fun onError_notCalledForValueEmission() = runBlockingTest {
        var receivedError = false

        channel.asFlow().onError { receivedError = true }.testCollect(this)
        channel.sendSingle("hi!")

        assertFalse(receivedError)
    }
}
