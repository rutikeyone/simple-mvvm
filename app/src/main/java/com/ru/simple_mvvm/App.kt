package com.ru.simple_mvvm

import android.app.Application
import com.ru.simple_mvvm.model.colors.InMemoryColorsRepository

class App : Application() {

    val models = mutableListOf<Any>(
        InMemoryColorsRepository()
    )

}