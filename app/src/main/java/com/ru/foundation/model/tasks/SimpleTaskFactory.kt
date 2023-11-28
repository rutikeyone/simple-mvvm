package com.ru.foundation.model.tasks

class SimpleTaskFactory : TaskFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SimpleTask(body)
    }

}