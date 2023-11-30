package com.ru.simple_mvvm

import android.app.Application
import com.ru.foundation.BaseApplication
import com.ru.foundation.model.dispatchers.MainThreadDispatcher
import com.ru.foundation.model.tasks.factories.ThreadTaskFactory
import com.ru.foundation.model.tasks.ThreadUtils
import com.ru.simple_mvvm.model.colors.InMemoryColorsRepository

class App : Application(), BaseApplication {

    private val taskFactory = ThreadTaskFactory();
    private val taskUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    private val coloRepository = InMemoryColorsRepository(taskFactory, taskUtils)

    override val singletonScopeDependencies = mutableListOf<Any>(
        taskFactory,
        dispatcher,
        coloRepository,
    )

}