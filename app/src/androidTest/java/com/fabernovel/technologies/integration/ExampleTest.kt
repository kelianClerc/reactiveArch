package com.fabernovel.technologies.integration

import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.fabernovel.technologies.app.example.ExampleActivity
import com.fabernovel.technologies.example
import com.fabernovel.technologies.utils.screenshot.ScreenshotRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleTest {

    @Rule @JvmField
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @Rule @JvmField
    var screenshotRule = ScreenshotRule()

    @Rule @JvmField
    var testRule = ActivityTestRule(ExampleActivity::class.java)

    @Test
    fun refreshACoupleOfTimes() {
        example {
            screenshotRule.screenshot("example")
            refresh()
        }
    }
}
