package com.fabernovel.technologies.app

import android.support.annotation.VisibleForTesting
import com.fabernovel.technologies.Settings
import com.squareup.leakcanary.LeakCanary
import io.reactivex.plugins.RxJavaPlugins
import org.reactivestreams.Subscriber
import timber.log.Timber

class DebugRandomUserReactiveApplication : RandomUserReactiveApplication() {

    override fun onCreate() {
        super.onCreate()
        setupLeakCanary()
        setupRxLogging()
    }

    private fun setupRxLogging() {
        RxJavaPlugins.setOnFlowableSubscribe { f, s -> object : Subscriber<Any> by s {
            override fun onError(t: Throwable?) {
                Timber.tag("Rx @${f.hashCode()} ERROR").v(t)
                s.onError(t)
            }

            override fun onNext(t: Any) {
                Timber.tag("Rx @${f.hashCode()} NEXT").v("$t")
                s.onNext(t)
            }
        } }
    }

    private fun setupLeakCanary() {
        if (!SHOW_MEMORY_LEAKS) {
            return
        }
        LeakCanary.install(this)
    }

    companion object {
        @VisibleForTesting
        var SHOW_MEMORY_LEAKS = Settings.performance.show_memory_leaks
    }
}
