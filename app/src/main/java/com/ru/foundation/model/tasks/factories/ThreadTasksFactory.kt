package com.ru.foundation.model.tasks.factories

import com.ru.foundation.model.tasks.AbstractTask
import com.ru.foundation.model.tasks.SynchronizedTask
import com.ru.foundation.model.tasks.Task
import com.ru.foundation.model.tasks.TaskListener


class ThreadTasksFactory : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ThreadTask(body))
    }

    private class ThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            thread = Thread {
                executeBody(body, listener)
            }
            thread?.start()
        }

        override fun doCancel() {
            thread?.interrupt()
        }

    }

}