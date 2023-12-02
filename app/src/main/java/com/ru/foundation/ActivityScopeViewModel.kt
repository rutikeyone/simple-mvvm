package com.ru.foundation

import androidx.lifecycle.ViewModel
import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.SideEffectMediatorsHolder

class ActivityScopeViewModel : ViewModel() {

    internal val sideEffectMediatorsHolder = SideEffectMediatorsHolder()

    val sideEffectMediators: List<SideEffectMediator<*>>
        get() = sideEffectMediatorsHolder.mediators

    override fun onCleared() {
        super.onCleared()
        sideEffectMediatorsHolder.clear()
    }

}