package com.fabernovel.technologies.di.crashes

import android.app.Application
import com.fabernovel.technologies.utils.HockeyAppCrashManagerListener
import dagger.Module
import dagger.Provides
import net.hockeyapp.android.CrashManagerListener

@Module
class CrashesModule {

    @Provides
    fun provideCrashListener(hockeyAppListener: HockeyAppCrashManagerListener): CrashManagerListener = hockeyAppListener

    @Provides
    fun provideActivityListener(hockeyAppListener: HockeyAppCrashManagerListener): Application.ActivityLifecycleCallbacks = hockeyAppListener
}
