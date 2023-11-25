package com.ru.simple_mvvm.model.colors

import com.ru.foundation.model.Repository

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    var currentColor: NamedColor

    fun getAvailableColor(): List<NamedColor>

    fun getById(id: Long): NamedColor

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)
}