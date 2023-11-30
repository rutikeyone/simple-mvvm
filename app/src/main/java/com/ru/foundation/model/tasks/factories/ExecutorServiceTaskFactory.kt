package com.ru.foundation.model.tasks.factories

import com.ru.foundation.model.tasks.AbstractTask
import com.ru.foundation.model.tasks.SynchronizedTask
import com.ru.foundation.model.tasks.Task
import com.ru.foundation.model.tasks.TaskListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class ExecutorServiceTaskFactory(
    private val executorService: ExecutorService,
) : TaskFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ExecutorServiceTask(body))
    }

    private inner class ExecutorServiceTask<T>(
        private val body: TaskBody<T>,
    ) : AbstractTask<T>() {

        private var future: Future<*>? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            future = executorService.submit {
                executeBody(body, listener)
            }
        }

        override fun doCancel() {
            future?.cancel(true)
        }

    }

}