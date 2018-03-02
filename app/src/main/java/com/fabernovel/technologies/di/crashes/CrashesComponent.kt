package com.fabernovel.technologies.di.crashes

import android.app.Application
import dagger.Subcomponent
import net.hockeyapp.android.CrashManagerListener

@Subcomponent(modules = [CrashesModule::class])
interface CrashesComponent {
    fun crashesListener(): CrashManagerListener
    fun activityListener(): Application.ActivityLifecycleCallbacks
}
