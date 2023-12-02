package com.ru.foundation.sideeffects.navigator

import com.ru.foundation.views.BaseScreen

interface Navigator {

    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)

}