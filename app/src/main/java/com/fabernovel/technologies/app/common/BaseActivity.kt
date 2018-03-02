package com.fabernovel.technologies.app.common

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import com.fabernovel.technologies.app.RandomUserReactiveScreen
import dagger.android.support.DaggerAppCompatActivity
import me.aartikov.alligator.NavigationContext
import me.aartikov.alligator.NavigationContextBinder
import timber.log.Timber
import javax.inject.Inject

abstract class BaseActivity<D, V : ReactiveViewModel<D>, S : RandomUserReactiveScreen> : DaggerAppCompatActivity() {

    @Inject lateinit var navigationContextBinder: NavigationContextBinder
    @Inject lateinit var navigationContext: NavigationContext

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var screen: S

    lateinit var viewModel: V

    abstract fun makeViewModel(provider: ViewModelProvider): V

    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(tag).d("onCreate (savedInstanceState=$savedInstanceState)")
        viewModel = makeViewModel(ViewModelProviders.of(this, viewModelFactory))
    }

    override fun onStart() {
        super.onStart()
        Timber.tag(tag).d("onStart")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Timber.tag(tag).d("onRestoreInstanceState (savedInstanceState=$savedInstanceState)")
    }

    override fun onResume() {
        Timber.tag(tag).d("onResume")
        super.onResume()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigationContextBinder.bind(navigationContext)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.tag(tag).d("onNewIntent (intent=$intent)")
    }

    override fun onPause() {
        Timber.tag(tag).d("onPause")
        navigationContextBinder.unbind()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Timber.tag(tag).d("onSaveInstanceState")
    }

    override fun onStop() {
        Timber.tag(tag).d("onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.tag(tag).d("onDestroy")
        super.onDestroy()
    }
}
