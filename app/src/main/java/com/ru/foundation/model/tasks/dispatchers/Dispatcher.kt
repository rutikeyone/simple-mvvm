package com.ru.foundation.model.tasks.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)

}