package com.ru.foundation.model.tasks.factories

import android.os.Handler
import android.os.HandlerThread
import com.ru.foundation.model.tasks.AbstractTask
import com.ru.foundation.model.tasks.SynchronizedTask
import com.ru.foundation.model.tasks.Task
import com.ru.foundation.model.tasks.TaskListener
import java.lang.IllegalStateException

class HandlerThreadTaskFactory: TaskFactory {

    private val thread = HandlerThread(javaClass.simpleName)
    private val handler = Handler(thread.looper)

    private var destroyed = false

    init {
        if (destroyed) throw IllegalStateException("Factory is closed")
        thread.start()
    }

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(HandlerThreadTask(body))
    }

    fun close() {
        destroyed = true
        thread.quitSafely()
    }

    private inner class HandlerThreadTask<T>(
        private val body: TaskBody<T>,
    ): AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            val runnable = Runnable {
                thread = Thread {
                   executeBody(body, listener)
                }.also {
                    it.start()
                    it.join()
                }
            }
            handler.post(runnable)
        }

        override fun doCancel() {
            thread?.interrupt()
        }

    }

}