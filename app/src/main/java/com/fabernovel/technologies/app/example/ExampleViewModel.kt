package com.fabernovel.technologies.app.example

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams.fromPublisher
import com.fabernovel.technologies.app.RandomUserReactiveScreen
import com.fabernovel.technologies.app.common.ReactiveViewModel
import com.fabernovel.technologies.app.common.UiState
import com.fabernovel.technologies.core.interactors.GetDateTime
import com.fabernovel.technologies.utils.i
import com.fabernovel.technologies.utils.toSimpleString
import io.reactivex.rxkotlin.ofType
import me.aartikov.alligator.Navigator
import javax.inject.Inject

class ExampleViewModel @Inject internal constructor(
    private val navigator: Navigator,
    interactor: GetDateTime
) : ReactiveViewModel<ExampleUiEvent>() {

    val content: LiveData<ExampleUiModel>
    val state: LiveData<UiState>

    init {
        val eventFlow = eventsProcessor
            .i("Rx event") { it.toSimpleString() }
            .share()

        val dataFlow = eventFlow
            .ofType<ExampleUiEvent.Refresh>()
            .map { GetDateTime.Request }
            .startWith(GetDateTime.Request)
            .compose(interactor)
            .share()

        val stateFlow = dataFlow
            .map { when(it) {
                GetDateTime.Response.InFlight -> UiState.Loading
                is GetDateTime.Response.Error -> UiState.Error(it.error)
                is GetDateTime.Response.Success -> UiState.Data
            } }
            .i("Rx state") { it.toSimpleString() }
            .cache()

        val contentFlow = dataFlow
            .ofType<GetDateTime.Response.Success>()
            .map { it.time }
            .map(::mapTime)
            .map { ExampleUiModel(it) }
            .i("Rx content") { it.toString() }
            .cache()

        val navigationFlow = eventFlow
            .ofType<ExampleUiEvent.Detail>()
            .map { RandomUserReactiveScreen.ExampleDetail(it.id) }
            .doOnNext { navigator.goForward(it) }

        content = fromPublisher(contentFlow)
        state = fromPublisher(stateFlow)
        disposables.add(navigationFlow.subscribe())
    }
}
