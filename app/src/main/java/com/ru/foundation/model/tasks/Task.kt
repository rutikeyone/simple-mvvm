package com.ru.foundation.model.tasks

import com.ru.foundation.model.FinalResult
import com.ru.foundation.model.dispatchers.Dispatcher

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    private val exception: Exception? = null,
) : Exception(exception)

interface Task<T> {

    fun await(): T

    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    fun cancel()

}