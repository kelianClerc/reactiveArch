package com.fabernovel.technologies.di.subcomponents

import android.app.Activity
import android.arch.lifecycle.ViewModel
import com.fabernovel.technologies.app.RandomUserReactiveScreen
import com.fabernovel.technologies.app.example.ExampleActivity
import com.fabernovel.technologies.app.example.ExampleViewModel
import com.fabernovel.technologies.di.common.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import me.aartikov.alligator.NavigationContext
import me.aartikov.alligator.ScreenResolver

@Module
class ExampleModule {

    @Provides
    fun provideActivity(activity: ExampleActivity): Activity = activity

    @Provides
    fun provideScreen(resolver: ScreenResolver, activity: ExampleActivity): RandomUserReactiveScreen.Example =
        resolver.getScreen(activity) ?: RandomUserReactiveScreen.Example

    @Provides
    fun provideNavigationContext(activity: ExampleActivity): NavigationContext =
        NavigationContext.Builder(activity).build()

    @Provides @IntoMap @ViewModelKey(ExampleViewModel::class)
    fun provideViewModel(viewModel: ExampleViewModel): ViewModel = viewModel
}
