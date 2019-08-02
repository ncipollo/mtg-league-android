package org.mtg.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy

abstract class MagicViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.dispose()
    }

    fun <T : Any> Observable<T>.toLiveData() = RxLiveData(this, compositeDisposable)

    fun <T : Any> Single<T>.toLiveData(): LiveData<T> {
        val liveData = MutableLiveData<T>()
        subscribeBy { liveData.postValue(it) }.also { compositeDisposable.add(it) }
        return liveData
    }

    fun <T : Any> Observable<T>.toSingleLiveEvent(): RxSingleLiveEvent<T> = RxSingleLiveEvent(this, compositeDisposable)
}
