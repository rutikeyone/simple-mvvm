package com.ru.foundation.views

import com.ru.foundation.ActivityScopeViewModel

interface FragmentHolder {

    fun notifyScreenChanged()

    fun getActivityScopeViewModel() : ActivityScopeViewModel

}