package com.ru.foundation

import android.content.Context
import androidx.annotation.MainThread
import java.lang.IllegalStateException

typealias SingletonFactory = (applicationContext: Context) -> List<Any>

object SingletonScopeDependencies {

    private var factory: SingletonFactory? = null
    private var dependencies: List<Any>? = null

    @MainThread
    fun init(factory: SingletonFactory) {
        if(this.factory != null) return
        this.factory = factory
    }

    @MainThread
    fun getSingletonScopeDependencies(applicationContext: Context): List<Any> {
        val factory = factory ?: throw IllegalStateException("Call init() before getting singleton dependencies")
        return dependencies ?: factory(applicationContext).also { dependencies = it }
    }

}