package com.ru.foundation.model.dispatchers

interface Dispatcher {

    fun dispatch(block: () -> Unit)

}