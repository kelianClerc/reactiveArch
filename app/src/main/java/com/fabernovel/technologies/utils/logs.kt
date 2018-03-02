package com.fabernovel.technologies.utils

import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.util.Log
import io.reactivex.Flowable
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Singleton

class LoggerComponentCallbacks : ComponentCallbacks2 {
    private val tag = "ComponentCallbacks2"

    override fun onLowMemory() = Timber.tag(tag).w("onLowMemory")

    override fun onConfigurationChanged(newConfig: Configuration?) =
        Timber.tag(tag).i("onConfigurationChanged (newConfig=$newConfig)")

    override fun onTrimMemory(level: Int) =
        Timber.tag(tag).w("onTrimMemory (level=$level)")
}

@Singleton
class InterceptorLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) = Timber.tag("InterceptorLogger").d(message)
}

// Extensions

fun <T : Any> Flowable<T>.d(tag: String = "Rx", message: (t: T) -> String): Flowable<T> =
    this.doOnNext { Timber.tag(tag).d(message(it)) }

fun <T : Any> Flowable<T>.e(tag: String = "Rx", message: (t: T) -> String): Flowable<T> =
    this.doOnNext { Timber.tag(tag).e(message(it)) }

fun <T : Any> Flowable<T>.i(tag: String = "Rx", message: (t: T) -> String): Flowable<T> =
    this.doOnNext { Timber.tag(tag).i(message(it)) }

fun <T : Any> Flowable<T>.log(tag: String = "Rx", priority: Int = Log.DEBUG, message: (t: T) -> String): Flowable<T> =
    this.doOnNext { Timber.tag(tag).log(priority, message(it)) }

fun <T : Any> Flowable<T>.v(tag: String = "Rx", message: (t: T) -> String): Flowable<T> =
    this.doOnNext { Timber.tag(tag).v(message(it)) }

fun <T : Any> Flowable<T>.w(tag: String = "Rx", message: (t: T) -> String): Flowable<T> =
    this.doOnNext { Timber.tag(tag).w(message(it)) }

fun <T : Any> Flowable<T>.wtf(tag: String = "Rx", message: (t: T) -> String): Flowable<T> =
    this.doOnNext { Timber.tag(tag).wtf(message(it)) }
