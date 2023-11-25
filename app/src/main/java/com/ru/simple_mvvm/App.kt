package com.ru.simple_mvvm

import android.app.Application
import com.ru.foundation.BaseApplication
import com.ru.foundation.model.Repository
import com.ru.simple_mvvm.model.colors.InMemoryColorsRepository

class App : Application(), BaseApplication {

    override val repositoryDependencies = mutableListOf<Repository>(
        InMemoryColorsRepository()
    )

}