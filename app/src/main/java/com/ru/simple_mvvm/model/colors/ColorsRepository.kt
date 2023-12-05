package com.ru.simple_mvvm.model.colors

import com.ru.foundation.model.Repository

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    suspend fun getAvailableColor(): List<NamedColor>

    suspend fun getById(id: Long): NamedColor

    suspend fun setCurrentColor(color: NamedColor)

    suspend fun getCurrentColor(): NamedColor

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)
}