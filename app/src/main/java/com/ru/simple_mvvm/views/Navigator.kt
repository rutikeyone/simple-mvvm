package com.ru.simple_mvvm.views

import com.ru.simple_mvvm.views.base.BaseScreen

interface Navigator {

    fun launch(screen: BaseScreen)

    fun navigateBack(result: Any? = null)

}