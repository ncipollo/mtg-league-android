package org.mtg.flow

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TestCollector<T> {
    private val values = mutableListOf<T>()
    private val throwableRef = AtomicReference<Throwable>()

    fun addElement(element: T) = synchronized(values) { values += element }


    fun setError(throwable: Throwable) = throwableRef.set(throwable)

    fun assertError(throwable: Throwable): TestCollector<T> {
        assertEquals(expected = throwable, actual = throwableRef.get())
        return this
    }

    fun assertNoError(): TestCollector<T> {
        assertNull(throwableRef.get())
        return this
    }

    fun assertNoValues(): TestCollector<T> {
        synchronized(this.values) { assertEquals(expected = emptyList<T>(), actual = this.values) }
        return this
    }

    fun assertValues(vararg values: T?): TestCollector<T> {
        synchronized(this.values) { assertEquals(expected = values.toList(), actual = this.values) }
        return this
    }
}

fun <T> Flow<T>.testCollect(scope: CoroutineScope): TestCollector<T> =
    TestCollector<T>().also { collector ->
        onEach { collector.addElement(it) }
            .catch { collector.setError(it) }
            .launchIn(scope)
    }
