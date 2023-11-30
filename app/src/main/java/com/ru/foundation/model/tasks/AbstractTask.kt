package com.ru.foundation.model.tasks

import com.ru.foundation.model.ErrorResult
import com.ru.foundation.model.FinalResult
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.model.dispatchers.Dispatcher
import com.ru.foundation.model.tasks.factories.TaskBody
import com.ru.foundation.utils.delegates.Await

abstract class AbstractTask<T> : Task<T> {

    private var finalResult by Await<FinalResult<T>>()

    final override fun await(): T {
        val wrapperListener: TaskListener<T> = {
            finalResult = it
        }
        doEnqueue(wrapperListener)

        try {
            when(val result = finalResult) {
                is ErrorResult -> throw result.exception
                is SuccessResult -> return result.data
            }
        } catch (e: Exception) {
            if(e is InterruptedException) {
                cancel()
                throw CancelledException(e)
            } else {
                throw e
            }
        }
    }

    final override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) {
        val wrapperListener: TaskListener<T> = {
            finalResult = it
            dispatcher.dispatch {
                listener.invoke(finalResult)
            }
        }
        doEnqueue(wrapperListener)
    }

    final override fun cancel() {
        finalResult = ErrorResult(CancelledException())
        doCancel()
    }

    fun executeBody(taskBody: TaskBody<T>, listener: TaskListener<T>) {
        try {
            val data = taskBody()
            listener(SuccessResult(data))
        } catch (e: Exception) {
            listener(ErrorResult(e))
        }
    }

    abstract fun doEnqueue(listener: TaskListener<T>)

    abstract fun doCancel()

}