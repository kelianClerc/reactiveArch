package com.fabernovel.technologies.di.common

import android.content.res.Resources

import dagger.Module
import dagger.Provides

@Module
class ResourcesModule(private val resources: Resources) {

    @Provides
    internal fun provideResources(): Resources {
        return resources
    }
}
