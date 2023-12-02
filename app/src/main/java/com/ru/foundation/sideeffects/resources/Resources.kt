package com.ru.foundation.sideeffects.resources

import androidx.annotation.StringRes
import com.ru.foundation.sideeffects.resources.plugin.ResourcesPlugin

interface Resources {

    fun getString(@StringRes resId: Int, vararg args: Any): String

}