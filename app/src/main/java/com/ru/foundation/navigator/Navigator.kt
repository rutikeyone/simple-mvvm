package com.ru.foundation.navigator

import com.ru.foundation.views.BaseScreen

interface Navigator {

    fun launch(screen: BaseScreen)

    fun navigateBack(result: Any? = null)

}