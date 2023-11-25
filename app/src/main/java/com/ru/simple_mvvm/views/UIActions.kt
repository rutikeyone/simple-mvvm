package com.ru.simple_mvvm.views

import androidx.annotation.StringRes

interface UIActions {

    fun toast(message: String)

    fun getString(@StringRes messageRes: Int, vararg args: Any): String
}