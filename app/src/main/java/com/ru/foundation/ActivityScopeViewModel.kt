package com.ru.foundation

import androidx.lifecycle.ViewModel
import com.ru.foundation.navigator.IntermediateNavigator
import com.ru.foundation.navigator.Navigator
import com.ru.foundation.uiactions.UiActions

const val ARG_SCREEN = "ARG_SCREEN"

class ActivityScopeViewModel(
    private val uiActions: UiActions,
    val navigator: IntermediateNavigator,
): ViewModel(), Navigator by navigator, UiActions by uiActions {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }
}