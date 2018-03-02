package com.squareup.picasso // Do not modify package.

import android.app.Activity
import android.os.Handler
import android.support.test.espresso.IdlingResource
import android.support.test.runner.lifecycle.ActivityLifecycleCallback
import android.support.test.runner.lifecycle.Stage
import java.lang.ref.WeakReference


class PicassoIdlingResource : IdlingResource, ActivityLifecycleCallback {

    private val idlePollDelayMillis = 50.toLong()

    private lateinit var callback: IdlingResource.ResourceCallback

    var picassoWeakReference: WeakReference<Picasso>? = null

    override fun getName(): String {
        return "PicassoIdlingResource"
    }

    override fun isIdleNow(): Boolean {
        if (isIdle()) {
            callback.onTransitionToIdle()
            return true
        }

       /* Force a re-check of the idle state in a little while.
        * If isIdleNow() returns false, Espresso only polls it every few seconds which can slow down our tests.
        */
        Handler().postDelayed({ isIdleNow }, idlePollDelayMillis)
        return false
    }

    private fun isIdle(): Boolean {
        return picassoWeakReference?.get()?.targetToAction?.isEmpty() ?: false
    }

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.callback = resourceCallback
    }

    override fun onActivityLifecycleChanged(activity: Activity, stage: Stage) {
        @Suppress("NON_EXHAUSTIVE_WHEN")
        when (stage) {
            Stage.RESUMED -> picassoWeakReference = WeakReference(Picasso.with(activity))
            Stage.PAUSED -> picassoWeakReference = null
        }
    }
}
