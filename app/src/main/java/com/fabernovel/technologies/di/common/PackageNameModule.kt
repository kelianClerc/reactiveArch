package com.fabernovel.technologies.di.common

import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PackageNameModule(private val packageName: String) {

    @Provides
    @Named("packageName")
    internal fun providePackageName(): String {
        return packageName
    }
}
