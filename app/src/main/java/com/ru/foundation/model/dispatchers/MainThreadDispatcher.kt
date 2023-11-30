package com.ru.foundation.model.dispatchers

import android.os.Handler
import android.os.Looper

class MainThreadDispatcher : Dispatcher {
    private val handler = Handler(Looper.getMainLooper())

    override fun dispatch(block: () -> Unit) {
        val isMainThread = handler.looper.thread.id == Thread.currentThread().id
        if(isMainThread) {
            block()
        } else {
            handler.post(block)
        }
    }

}