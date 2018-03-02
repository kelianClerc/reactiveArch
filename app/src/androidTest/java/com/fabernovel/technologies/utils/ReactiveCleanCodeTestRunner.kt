package com.fabernovel.technologies.utils

import android.os.Bundle
import android.support.test.espresso.IdlingRegistry
import android.support.test.runner.AndroidJUnitRunner
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import com.squareup.picasso.PicassoIdlingResource
import com.squareup.rx2.idler.Rx2Idler
import io.reactivex.plugins.RxJavaPlugins

class RandomUserReactiveTestRunner : AndroidJUnitRunner() {

    private val picassoIdlingResource = PicassoIdlingResource()

    override fun onStart() {
        setupSchedulerIdlers()
        setupPicassoIdlers()
        super.onStart()
    }

    private fun setupPicassoIdlers() {
        IdlingRegistry.getInstance().register(picassoIdlingResource)
        ActivityLifecycleMonitorRegistry.getInstance().addLifecycleCallback(picassoIdlingResource)
    }

    private fun setupSchedulerIdlers() {
        RxJavaPlugins.setInitComputationSchedulerHandler(Rx2Idler.create("Computation Scheduler"))
        RxJavaPlugins.setInitIoSchedulerHandler(Rx2Idler.create("IO Scheduler"))
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        IdlingRegistry.getInstance().unregister(picassoIdlingResource)
        super.finish(resultCode, results)
    }
}
