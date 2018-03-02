package com.fabernovel.technologies.di.common

import com.fabernovel.technologies.app.AlligatorLogger
import com.fabernovel.technologies.app.RandomUserReactiveNavigationFactory
import dagger.Module
import dagger.Provides
import me.aartikov.alligator.NavigationContextBinder
import me.aartikov.alligator.Navigator
import me.aartikov.alligator.ScreenResolver
import javax.inject.Singleton


@Module
class NavigationModule {

    @Provides
    @Singleton
    fun provideAndroidNavigator(): AlligatorLogger =
        AlligatorLogger(RandomUserReactiveNavigationFactory())

    @Provides
    fun provideNavigator(navigator: AlligatorLogger): Navigator = navigator

    @Provides
    fun provideBinder(navigator: AlligatorLogger): NavigationContextBinder = navigator

    @Provides
    fun provideScreenResolver(navigator: AlligatorLogger): ScreenResolver = navigator.screenResolver

}
