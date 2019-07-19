package org.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mtg.viewmodel.MagicViewModel

@RunWith(JUnit4::class)
class MagicViewModelTest {
    @get:Rule
    val taskRule = InstantTaskExecutorRule()

    private val lifecycleOwner = TestLifeCycleOwner.started()
    private val observer = TestObserver<Int>()
    private val viewModel = TestViewModel()

    @Test
    fun onCleared() {
        viewModel.observableLiveData.observe(lifecycleOwner, observer)
        viewModel.onCleared()
        viewModel.subject.onNext(2)
        viewModel.subject.onNext(3)
        observer.assertValues()
    }

    @Test
    fun observer_toLiveData() {
        viewModel.observableLiveData.observe(lifecycleOwner, observer)
        viewModel.subject.onNext(2)
        viewModel.subject.onNext(3)
        observer.assertValues(2, 3)
    }

    @Test
    fun single_toLiveData() {
        viewModel.singleLiveData.observe(lifecycleOwner, observer)
        viewModel.subject.onNext(2)
        observer.assertValues(2)
    }

    private class TestViewModel : MagicViewModel() {
        val subject = PublishSubject.create<Int>()
        val observableLiveData = subject.toLiveData()
        val singleLiveData = subject.firstOrError().toLiveData()

        public override fun onCleared() {
            super.onCleared()
        }
    }

}
