package com.fabernovel.technologies.utils.helpers

import android.app.Instrumentation
import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.matcher.ViewMatchers.*
import android.view.View
import android.view.ViewGroup
import com.fabernovel.technologies.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import java.io.File

fun resetSharedPreferences() {
    val targetContext = InstrumentationRegistry.getTargetContext()
    val root = targetContext.filesDir.parentFile
    val sharedPrefsFileNames = File(root, "shared_prefs").list()
    sharedPrefsFileNames
        .map { it.replace(".xml", "") }
        .map { targetContext.getSharedPreferences(it, 0) }
        .forEach { it.edit().clear().apply() }
}

fun clickPasswordToggle(): ViewAction {
    return object : ViewAction {

        override fun getConstraints(): Matcher<View> {
            return isAssignableFrom(TextInputLayout::class.java)
        }

        override fun getDescription(): String {
            return "Clicks the password toggle"
        }

        override fun perform(uiController: UiController, view: View) {
            val textInputLayout = view as TextInputLayout
            // Reach in and find the password toggle since we don't have a public API
            // to get a reference to it
            val passwordToggle = textInputLayout.findViewById<View>(R.id.text_input_password_toggle)
            passwordToggle.performClick()
        }
    }
}

fun click(id: Int) {
    onView(withId(id))
        .check(matches(isDisplayed()))
        .perform(ViewActions.click())
}

fun hasToolbarTitle(title: Int) {
    onView(withId(R.id.toolbar))
        .check { _, _ ->
            matches(Matchers.allOf<View>(isDisplayed(), withChild(withText(title))))
        }
}

fun hasToolbarTitle(title: String) {
    onView(withId(R.id.toolbar))
        .check { _, _ ->
            matches(Matchers.allOf<View>(isDisplayed(), withChild(withText(title))))
        }
}

fun toolbarNavigation() = onView(Matchers.allOf(
    withParent(withId(R.id.toolbar)),
    withClassName(Matchers.`is`("android.support.v7.widget.AppCompatImageButton"))))

fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("with $childPosition child view of type parentMatcher")
        }

        override fun matchesSafely(view: View): Boolean {
            if (view.parent !is ViewGroup) {
                return parentMatcher.matches(view.parent)
            }

            val group = view.parent as ViewGroup
            return parentMatcher.matches(view.parent) && view == group.getChildAt(childPosition)
        }
    }
}

fun intentAsserter(maker: () -> Matcher<Intent>): (f:() -> Unit) -> Unit = { f ->
    val expected = maker()
    val result = Instrumentation.ActivityResult(0, null)
    Intents.intending(expected).respondWith(result)
    f()
    Intents.intended(expected)
}
