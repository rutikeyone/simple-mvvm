package com.ru.simple_mvvm.views.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ru.simple_mvvm.utils.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

open class BaseViewModel : ViewModel() {

    open fun onResult(result: Any) {}

}