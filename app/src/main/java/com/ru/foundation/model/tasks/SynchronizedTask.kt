package com.ru.foundation.model.tasks

import com.ru.foundation.model.dispatchers.Dispatcher
import java.lang.IllegalStateException
import java.util.concurrent.atomic.AtomicBoolean

class SynchronizedTask<T>(
    private val task: Task<T>,
) : Task<T> {

    @Volatile
    private var cancelled = false
    private var executed = false

    private var listenerCancelled = AtomicBoolean(false)

    override fun await(): T {
        synchronized(this) {
            if (cancelled) throw CancelledException()
            if (executed) throw IllegalStateException("Task has been executed")
            executed = true
        }
        return task.await()
    }

    override fun cancel() = synchronized(this) {
        if(listenerCancelled.compareAndSet(false, true)) {
            if(cancelled) return
            cancelled = true
            task.cancel();
        }
    }

    override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) = synchronized(this) {
        if(cancelled) throw CancelledException()
        if(executed) throw IllegalStateException("Task has been executed")
        executed = true

        val finalListener: TaskListener<T> = { result ->
            if(listenerCancelled.compareAndSet(false, true)) {
                if(!cancelled) listener.invoke(result)
            }
        }

        task.enqueue(dispatcher, finalListener)
    }

}