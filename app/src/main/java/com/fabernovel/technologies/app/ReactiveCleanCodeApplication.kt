package com.fabernovel.technologies.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.fabernovel.technologies.BuildConfig
import com.fabernovel.technologies.Settings
import com.fabernovel.technologies.core.RandomUserReactiveError
import com.fabernovel.technologies.di.ComponentManager
import com.fabernovel.technologies.utils.HockeyAppTree
import com.fabernovel.technologies.utils.LoggerComponentCallbacks
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import net.danlew.android.joda.JodaTimeAndroid
import net.hockeyapp.android.CrashManager
import timber.log.Timber
import javax.inject.Inject

open class RandomUserReactiveApplication : Application(), HasActivityInjector {

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        registerComponentCallbacks(LoggerComponentCallbacks())
        setupGraph()
        setupLogging()
        setupHockeyApp()
        setupJoda()
        setupRxErrorHandling()
    }

    override fun activityInjector() = dispatchingActivityInjector

    private fun setupRxErrorHandling() {
        RxJavaPlugins.setErrorHandler {
            when (it) {
                is UndeliverableException -> when (it.cause) {
                    is RandomUserReactiveError -> Timber.w(it, "Unhandled error.")
                    else -> throw it
                }
                is RandomUserReactiveError -> Timber.w(it, "Unhandled error.")
                else -> throw it
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    private fun setupJoda() {
        JodaTimeAndroid.init(this)
    }

    private fun setupHockeyApp() {
        if (!HOCKEY_APP_ENABLED) {
            return
        }
        val component = ComponentManager.crashesComponent
        CrashManager.register(this, HOCKEY_APP_KEY, component.crashesListener())
        registerActivityLifecycleCallbacks(component.activityListener())
    }

    protected fun setupGraph() {
        val preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        ComponentManager.init(preferences, cacheDir, applicationContext)
        ComponentManager.applicationComponent.inject(this)
    }

    private fun setupLogging() {
        if (HOCKEY_APP_ENABLED) Timber.plant(HockeyAppTree())
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    companion object {
        protected val PREFS_NAME = "default"
        protected val HOCKEY_APP_ENABLED: Boolean = Settings.crashes.enabled
        protected val HOCKEY_APP_KEY: String = Settings.crashes.app_id
    }
}
