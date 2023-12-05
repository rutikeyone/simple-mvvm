package com.ru.simple_mvvm

import android.app.Application
import com.ru.foundation.BaseApplication
import com.ru.foundation.model.coroutines.CoroutineDispatchers
import com.ru.foundation.model.dispatchers.MainThreadDispatcher
import com.ru.simple_mvvm.model.colors.InMemoryColorsRepository

class App : Application(), BaseApplication {

    private val ioDispatcher = CoroutineDispatchers.IODispatcher()
    private val coloRepository = InMemoryColorsRepository(ioDispatcher)

    override val singletonScopeDependencies = mutableListOf(
        coloRepository,
    )

}