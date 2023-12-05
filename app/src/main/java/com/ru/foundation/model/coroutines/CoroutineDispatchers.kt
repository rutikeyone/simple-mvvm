package com.ru.foundation.model.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

sealed class CoroutineDispatchers(
    val value: CoroutineDispatcher
) {

    class WorkerDispatcher: CoroutineDispatchers( Dispatchers.Default)

    class IODispatcher: CoroutineDispatchers(Dispatchers.IO)

}