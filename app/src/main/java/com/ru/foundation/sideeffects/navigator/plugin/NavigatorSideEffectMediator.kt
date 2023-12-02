package com.ru.foundation.sideeffects.navigator.plugin

import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.navigator.Navigator
import com.ru.foundation.views.BaseScreen

class NavigatorSideEffectMediator : SideEffectMediator<Navigator>(), Navigator {

    override fun launch(screen: BaseScreen) = target {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = target {
        it.goBack(result)
    }

}