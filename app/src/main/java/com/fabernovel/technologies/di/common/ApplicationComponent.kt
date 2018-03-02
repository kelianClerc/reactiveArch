package com.fabernovel.technologies.di.common

import com.fabernovel.technologies.app.RandomUserReactiveApplication
import com.fabernovel.technologies.di.crashes.CrashesComponent
import com.fabernovel.technologies.di.crashes.CrashesModule
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import me.aartikov.alligator.NavigationContextBinder
import me.aartikov.alligator.Navigator
import javax.inject.Singleton

@Singleton
@Component(modules = [
    PreferencesModule::class,
    ServiceModule::class,
    RepositoryModule::class,
    AndroidSupportInjectionModule::class,
    AndroidBindingModule::class,
    NavigationModule::class
])
interface ApplicationComponent {
    fun inject(application: RandomUserReactiveApplication)

    fun navigator(): Navigator
    fun navigatorContextBinder(): NavigationContextBinder

    operator fun plus(module: CrashesModule): CrashesComponent
}
