package com.ru.foundation.model.tasks

import com.ru.foundation.model.FinalResult
import com.ru.foundation.model.tasks.dispatchers.Dispatcher

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    originException: Exception? = null
) : Exception(originException)

interface Task<T> {

    fun await(): T

    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    fun cancel()

}