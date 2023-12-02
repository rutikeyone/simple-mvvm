package com.ru.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.Result
import com.ru.foundation.model.tasks.Task
import com.ru.foundation.model.tasks.TaskListener
import com.ru.foundation.model.tasks.dispatchers.Dispatcher
import com.ru.foundation.utils.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

open class BaseViewModel(
    private val dispatcher: Dispatcher
) : ViewModel() {

    private val tasks = mutableSetOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        clearTasks()
    }

    open fun onResult(result: Any) {

    }

    open fun onBackPressed(): Boolean {
        clearTasks()
        return false
    }

    fun <T> Task<T>.safeEnqueue(listener: TaskListener<T>? = null) {
        tasks.add(this)
        this.enqueue(dispatcher) {
            tasks.remove(this)
            listener?.invoke(it)
        }
    }

    fun <T> Task<T>.into(liveResult: MutableLiveResult<T>) {
        liveResult.value = PendingResult()
        this.safeEnqueue {
            liveResult.value = it
        }
    }

    private fun clearTasks() {
        tasks.forEach { it.cancel() }
        tasks.clear()
    }

}