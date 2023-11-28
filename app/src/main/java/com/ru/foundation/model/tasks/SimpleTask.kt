package com.ru.foundation.model.tasks

import android.os.Handler
import android.os.Looper
import com.ru.foundation.model.ErrorResult
import com.ru.foundation.model.FinalResult
import com.ru.foundation.model.SuccessResult
import java.lang.Exception

class SimpleTask<T>(
    private val body: TaskBody<T>
): Task<T> {

    private val handler = Handler(Looper.getMainLooper())

    private var thread: Thread? = null
    private var cancelled = false

    override fun await(): T = body()

    override fun cancel() {
        cancelled = true
        thread?.interrupt()
        thread = null
    }

    override fun enqueue(listener: TaskListener<T>) {
        thread = Thread {
            try {
                val data = body()
                publishResult(listener, SuccessResult(data))
            } catch (e: Exception) {
                publishResult(listener, ErrorResult(e))
            }
        }.also { it.start() }
    }

    private fun publishResult(listener: TaskListener<T>, result: FinalResult<T>) {
        handler.post {
            if(cancelled) return@post
            listener(result)
        }
    }

}