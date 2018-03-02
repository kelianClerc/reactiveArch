package com.fabernovel.technologies.utils

import android.app.Activity
import android.support.test.espresso.intent.rule.IntentsTestRule
import com.fabernovel.technologies.utils.helpers.resetSharedPreferences

class ResetPrefsTestRule<T : Activity?>(activityClass: Class<T>?) : IntentsTestRule<T>(activityClass) {
    override fun beforeActivityLaunched() {
        resetSharedPreferences()
    }
}
