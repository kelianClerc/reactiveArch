package com.fabernovel.technologies.utils

import android.os.Environment
import android.support.test.internal.runner.listener.InstrumentationRunListener
import android.support.test.runner.permission.PermissionRequester
import android.util.Log
import com.fabernovel.technologies.utils.screenshot.Constants
import org.junit.runner.Description

class ClearSpoonScreenshotsRunListener : InstrumentationRunListener() {

    override fun testRunStarted(description: Description?) {
        super.testRunStarted(description)
        clearSpoonScreenshots()
    }

    private val TAG = "ClearSpoonScreenshotsRunListener"

    fun clearSpoonScreenshots() {
        val requester = PermissionRequester()
        requester.addPermissions(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
        requester.requestPermissions()
        val root = Environment.getExternalStoragePublicDirectory(Constants.SPOON_SCREENSHOTS_FOLDER)
        val deleteRecursively = root.deleteRecursively()
        val result = if (deleteRecursively) "success" else "failure"
        Log.i(TAG, "clearing screenshots from folder ${root.absolutePath} $result")
    }

}
