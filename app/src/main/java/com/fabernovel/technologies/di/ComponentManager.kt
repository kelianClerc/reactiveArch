package com.fabernovel.technologies.di

import android.content.Context
import android.content.SharedPreferences
import com.fabernovel.technologies.di.common.ApplicationComponent
import com.fabernovel.technologies.di.common.DaggerApplicationComponent
import com.fabernovel.technologies.di.common.PreferencesModule
import com.fabernovel.technologies.di.common.ServiceModule
import com.fabernovel.technologies.di.crashes.CrashesComponent
import com.fabernovel.technologies.di.crashes.CrashesModule
import java.io.File

object ComponentManager {

    internal lateinit var applicationComponent: ApplicationComponent
    internal lateinit var crashesComponent: CrashesComponent

    fun init(
        preferences: SharedPreferences, cacheDirectory: File, applicationContext: Context
    ) {
        val crashesModule = CrashesModule()
        val preferencesModule = PreferencesModule(preferences)
        val serviceModule = ServiceModule(cacheDirectory, applicationContext)
        initApplicationComponent(preferencesModule, serviceModule)
        initCrashesComponent(crashesModule)
    }

    private fun initApplicationComponent(
        preferencesModule: PreferencesModule,
        serviceModule: ServiceModule
    ) {
        applicationComponent = DaggerApplicationComponent
            .builder()
            .preferencesModule(preferencesModule)
            .serviceModule(serviceModule)
            .build()
    }

    private fun initCrashesComponent(crashesModule: CrashesModule) {
        crashesComponent = applicationComponent.plus(crashesModule)
    }
}
