package org.mtg.flow

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

fun <T> Flow<T>?.testChannel(): SendChannel<T> =
    BroadcastChannel<T>(Channel.BUFFERED).also { whenever(this) doReturn it.asFlow() }

suspend fun <T> SendChannel<T>.sendSingle(element: T) {
    send(element)
    close()
}
