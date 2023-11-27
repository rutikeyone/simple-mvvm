package com.ru.simple_mvvm.model.colors

import com.ru.foundation.model.Repository
import com.ru.foundation.model.tasks.Task
import java.util.jar.Attributes.Name

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    fun getAvailableColor(): Task<List<NamedColor>>

    fun getById(id: Long): Task<NamedColor>

    fun setCurrentColor(color: NamedColor): Task<Unit>

    fun getCurrentColor(): Task<NamedColor>

    fun addListener(listener: ColorListener)

    fun removeListener(listener: ColorListener)
}