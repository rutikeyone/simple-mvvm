package com.ru.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ru.foundation.model.ErrorResult
import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.Result
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

open class BaseViewModel: ViewModel() {

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate
    protected val viewModelScope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        cancelCoroutineScope()
    }

    open fun onResult(result: Any) {}

    open fun onBackPressed(): Boolean {
        cancelCoroutineScope()
        return false
    }

    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        liveResult.value = PendingResult()
        viewModelScope.launch {
            try {
                liveResult.value = SuccessResult(block())
            } catch (e: Exception) {
                liveResult.value = ErrorResult(e)
            }
        }
    }

    private fun cancelCoroutineScope() = viewModelScope.cancel()

}