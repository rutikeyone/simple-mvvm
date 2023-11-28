package com.ru.foundation.model.tasks

import com.ru.foundation.model.Repository

typealias TaskBody<T> = () -> T

interface TaskFactory : Repository {

    fun <T> async(body: TaskBody<T>): Task<T>

}