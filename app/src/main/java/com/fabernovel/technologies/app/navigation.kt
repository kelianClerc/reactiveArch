package com.fabernovel.technologies.app

import com.fabernovel.technologies.app.example.ExampleActivity
import me.aartikov.alligator.AndroidNavigator
import me.aartikov.alligator.NavigationFactory
import me.aartikov.alligator.Screen
import me.aartikov.alligator.navigationfactories.RegistryNavigationFactory
import timber.log.Timber
import java.io.Serializable


sealed class RandomUserReactiveScreen : Screen, Serializable {

    // Add new screens here.
    // Use sealed classes when needed to make a hierarchy.
    // Use object for screens without data.
    // Use data classes for screens that need data.

    object Example : RandomUserReactiveScreen()
    data class ExampleDetail(val id: String) : RandomUserReactiveScreen()
}

class RandomUserReactiveNavigationFactory : RegistryNavigationFactory() {

    init {
        // Register each screen added above with
        // its corresponding Activity, Fragment or custom function.

        registerActivity(RandomUserReactiveScreen.Example::class.java, ExampleActivity::class.java)
    }
}

class AlligatorLogger(navigationFactory: NavigationFactory) : AndroidNavigator(navigationFactory) {

    override fun goForward(screen: Screen?) {
        Timber.tag("Alligator navigation").i("Go forward with screen : $screen")
        super.goForward(screen)
    }

    override fun goBack() {
        Timber.tag("Alligator navigation").i("Go back")
        super.goBack()
    }


    override fun replace(screen: Screen?) {
        Timber.tag("Alligator navigation").i("Replace with screen : $screen")
        super.replace(screen)
    }
}



