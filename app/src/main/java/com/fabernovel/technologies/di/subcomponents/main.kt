package com.fabernovel.technologies.di.subcomponents

import android.app.Activity
import android.arch.lifecycle.ViewModel
import com.fabernovel.technologies.app.RandomUserReactiveScreen
import com.fabernovel.technologies.app.main.MainActivity
import com.fabernovel.technologies.app.main.MainViewModel
import com.fabernovel.technologies.di.common.ViewModelKey
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import me.aartikov.alligator.NavigationContext
import me.aartikov.alligator.ScreenResolver


@Module
class MainModule {

    @Provides
    fun provideActivity(activity: MainActivity): Activity = activity

    @Provides
    fun provideScreen(resolver: ScreenResolver, activity: MainActivity): RandomUserReactiveScreen.Main =
        resolver.getScreen(activity) ?: RandomUserReactiveScreen.Main

    @Provides
    fun provideNavigationContext(activity: MainActivity): NavigationContext =
        NavigationContext.Builder(activity).build()

    @Provides
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun provideViewModel(viewModel: MainViewModel): ViewModel = viewModel
}
