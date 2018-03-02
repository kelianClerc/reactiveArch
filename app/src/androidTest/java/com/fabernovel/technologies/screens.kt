package com.fabernovel.technologies

import com.fabernovel.technologies.utils.helpers.click
import com.fabernovel.technologies.utils.helpers.hasToolbarTitle

fun example(block: ExampleScreen.() -> Unit) = ExampleScreen().apply { block() }

class ExampleScreen {
    init {
        hasToolbarTitle(R.string.app_name)
    }

    fun refresh() = click(R.id.button)
}
