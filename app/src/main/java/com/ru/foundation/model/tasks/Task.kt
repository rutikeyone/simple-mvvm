package com.ru.foundation.model.tasks

import com.ru.foundation.model.FinalResult

typealias TaskListener<T> = (FinalResult<T>) -> Unit

interface Task<T> {

    fun await(): T

    fun enqueue(listener: TaskListener<T>)

    fun cancel()

}