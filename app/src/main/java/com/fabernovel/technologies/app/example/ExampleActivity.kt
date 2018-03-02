package com.fabernovel.technologies.app.example

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import com.fabernovel.technologies.R
import com.fabernovel.technologies.app.RandomUserReactiveScreen
import com.fabernovel.technologies.app.common.BaseActivity
import com.fabernovel.technologies.app.common.UiState
import com.fabernovel.technologies.utils.ThemeUtils
import com.fabernovel.technologies.utils.observe
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : BaseActivity<ExampleUiEvent, ExampleViewModel, RandomUserReactiveScreen.Example>() {

    override fun makeViewModel(provider: ViewModelProvider): ExampleViewModel =
        provider.get(ExampleViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeUtils.ensureRuntimeTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)
        bindViewModel()
        bindEvents()
    }

    private fun bindViewModel() {
        viewModel.state.observe(this, this::showState)
        viewModel.content.observe(this, this::showContent)
    }

    private fun bindEvents() {
        val buttonEvent = button.clicks().map { ExampleUiEvent.Refresh as ExampleUiEvent }
        val userButtonEvent = goToUser.clicks().map { ExampleUiEvent.GoToUsers as ExampleUiEvent }
        viewModel.events = Observable.merge(buttonEvent, userButtonEvent)
    }

    private fun showState(state: UiState) = when (state) {
        UiState.Loading -> stateful.showLoading()
        is UiState.Error -> stateful.showError(R.string.generic_error_message, null)
        UiState.Data -> stateful.showContent()
        UiState.Empty -> stateful.showEmpty()
    }

    private fun showContent(content: ExampleUiModel) {
        message.text = content.id
    }
}
