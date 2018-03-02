package com.fabernovel.technologies.app.common

import android.arch.lifecycle.ViewModel
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor

open class ReactiveViewModel<T> : ViewModel() {

    protected val eventsProcessor: PublishProcessor<T> = PublishProcessor.create<T>()

    var events: Observable<T> = Observable.empty()
    set(events) = events.toFlowable(BackpressureStrategy.LATEST).subscribe(eventsProcessor)

    protected val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
