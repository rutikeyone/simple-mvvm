package com.ru.simple_mvvm

import com.ru.foundation.SingletonScopeDependencies
import com.ru.foundation.model.coroutines.CoroutineDispatchers
import com.ru.simple_mvvm.model.colors.InMemoryColorsRepository

object Initializer {

    fun initDependencies() = SingletonScopeDependencies.init { _ ->
        val ioDispatcher = CoroutineDispatchers.IODispatcher()
        val coloRepository = InMemoryColorsRepository(ioDispatcher)

        return@init listOf(
            coloRepository,
        )
    }

}