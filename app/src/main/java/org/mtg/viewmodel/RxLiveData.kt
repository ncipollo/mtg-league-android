package org.mtg.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

class RxLiveData<T>(
    private val source: Observable<T>,
    private val compositeDisposable: CompositeDisposable
) : LiveData<T>() {
    private var subscribed = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, observer)
        if (!subscribed) {
            val disposable = source.subscribe { postValue(it) }
            compositeDisposable.add(disposable)
            subscribed = true
        }
    }

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) =
        observe(owner, Observer { observer(it!!) })
}
