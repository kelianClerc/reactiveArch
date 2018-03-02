package com.fabernovel.technologies.utils;

import android.os.Bundle
import android.support.test.runner.AndroidJUnitRunner
import com.fabernovel.technologies.app.DebugRandomUserReactiveApplication

class RandomUserReactiveRunner : AndroidJUnitRunner() {

    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        disableDebugTools()
    }

    private fun disableDebugTools() {
        DebugRandomUserReactiveApplication.SHOW_MEMORY_LEAKS = false
    }
}
