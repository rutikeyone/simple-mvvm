package com.ru.simple_mvvm.model.colors

import com.ru.foundation.model.Repository
import kotlinx.coroutines.flow.Flow

typealias ColorListener = (NamedColor) -> Unit

interface ColorsRepository : Repository {

    suspend fun getAvailableColor(): List<NamedColor>

    suspend fun getById(id: Long): NamedColor

    fun setCurrentColor(color: NamedColor): Flow<Int>

    suspend fun getCurrentColor(): NamedColor

    fun listenCurrentColor(): Flow<NamedColor>
}