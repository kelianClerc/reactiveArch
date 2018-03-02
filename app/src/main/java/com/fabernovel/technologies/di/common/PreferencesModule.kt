package com.fabernovel.technologies.di.common

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferencesModule(private val preferences: SharedPreferences) {

    @Provides
    @Singleton
    internal fun preferences(): SharedPreferences {
        return preferences
    }
}
