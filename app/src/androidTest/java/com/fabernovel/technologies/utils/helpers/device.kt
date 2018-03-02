package com.fabernovel.technologies.utils.helpers

import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.uiautomator.By
import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.Until
import org.hamcrest.CoreMatchers

private val NOTIFICATIONS = "com.android.systemui:id/notification_stack_scroller"
private val CLEAR = "com.android.systemui:id/dismiss_text"
private val RECENTS = "com.android.systemui:id/recents_view"
private val TASK = "com.android.systemui:id/task_view_bar"

fun UiDevice.showNotifications() {
    openNotification()
    wait(Until.hasObject(By.res(NOTIFICATIONS)), 5000)
}

fun UiDevice.clearNotifications() {
    findObject(By.res(CLEAR)).click()
    waitForIdle()
}

fun UiDevice.clickNotification(index: Int) {
    findObject(By.res(NOTIFICATIONS))
        .children[0].click()
}

fun UiDevice.goHome() {
    pressHome()
    waitForHome(this)
}

fun UiDevice.restartActivityFromRecents() {
    goHome()
    pressRecentApps()
    wait(Until.hasObject(By.res(RECENTS)), 5000)
    findObjects(By.res(TASK)).last().click()
}

private fun waitForHome(device: UiDevice) {
    val launcherPackage = device.launcherPackageName
    ViewMatchers.assertThat(launcherPackage, CoreMatchers.notNullValue())
    device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), 5000)
}
