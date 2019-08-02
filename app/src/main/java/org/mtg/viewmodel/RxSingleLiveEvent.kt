package org.mtg.viewmodel

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.atomic.AtomicBoolean

class RxSingleLiveEvent<T>(
    private val source: Observable<T>,
    private val compositeDisposable: CompositeDisposable
) : LiveData<T>() {
    private var subscribed = false
    private val pending = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
        if (!subscribed) {
            val disposable = source.subscribe { postValue(it) }
            compositeDisposable.add(disposable)
            subscribed = true
        }
    }

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        observe(owner, Observer { observer(it!!) })

    @MainThread
    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}

