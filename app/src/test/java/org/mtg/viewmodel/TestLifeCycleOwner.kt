package org.mtg.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class TestLifeCycleOwner: LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle() = lifecycleRegistry

    companion object {
        fun started() = TestLifeCycleOwner().apply { onStart() }
    }

    fun onStart() {
        lifecycleRegistry.markState(Lifecycle.State.CREATED)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }
}
