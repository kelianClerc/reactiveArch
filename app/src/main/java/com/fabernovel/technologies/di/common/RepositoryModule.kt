package com.fabernovel.technologies.di.common

import com.fabernovel.technologies.core.ExampleRepository
import com.fabernovel.technologies.data.StubExampleRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    internal abstract fun bindExampleRepository(instance: StubExampleRepository): ExampleRepository
}
