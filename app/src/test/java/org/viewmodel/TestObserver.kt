package org.viewmodel

import androidx.lifecycle.Observer
import org.junit.Assert

class TestObserver<T>: Observer<T> {
    private val values = mutableListOf<T?>()

    override fun onChanged(value: T?) {
        values += value
    }

    fun assertValues(vararg values: T?) = Assert.assertEquals(values.toList(), this.values)
}
