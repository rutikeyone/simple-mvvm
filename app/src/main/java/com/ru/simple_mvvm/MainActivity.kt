package com.ru.simple_mvvm

import android.os.Bundle
import com.ru.foundation.sideeffects.SideEffectPluginsManager
import com.ru.foundation.sideeffects.dialogs.plugin.DialogsPlugin
import com.ru.foundation.sideeffects.intents.plugin.IntentsPlugin
import com.ru.foundation.sideeffects.navigator.plugin.NavigatorPlugin
import com.ru.foundation.sideeffects.navigator.plugin.StackFragmentNavigator
import com.ru.foundation.sideeffects.permissions.plugin.PermissionsPlugin
import com.ru.foundation.sideeffects.resources.plugin.ResourcesPlugin
import com.ru.foundation.sideeffects.toasts.plugin.ToastsPlugin
import com.ru.foundation.views.activity.BaseActivity
import com.ru.simple_mvvm.views.current_color.CurrentColorFragment


class MainActivity : BaseActivity() {

    override fun registerPlugins(manager: SideEffectPluginsManager) = with (manager) {
        val navigator = createNavigator()
        register(ToastsPlugin())
        register(ResourcesPlugin())
        register(NavigatorPlugin(navigator))
        register(PermissionsPlugin())
        register(DialogsPlugin())
        register(IntentsPlugin())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Initializer.initDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun createNavigator() = StackFragmentNavigator(
        containerId = R.id.fragmentContainer,
        defaultTitle = getString(R.string.app_name),
        animations = StackFragmentNavigator.Animations(
            enterAnim = R.anim.enter,
            exitAnim = R.anim.exit,
            popEnterAnim = R.anim.pop_enter,
            popExitAnim = R.anim.pop_exit
        ),
        initialScreenCreator = { CurrentColorFragment.Screen() }
    )
}