package com.ru.foundation.sideeffects.navigator.plugin

import android.content.Context
import com.ru.foundation.sideeffects.SideEffectPlugin
import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.navigator.Navigator


class NavigatorPlugin(
    private val navigator: Navigator,
) : SideEffectPlugin<Navigator, Navigator> {

    override val mediatorClass: Class<Navigator>
        get() = Navigator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Navigator> {
        return NavigatorSideEffectMediator()
    }

    override fun createImplementation(mediator: Navigator): Navigator {
        return navigator
    }

}