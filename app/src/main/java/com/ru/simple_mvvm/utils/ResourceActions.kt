package com.ru.simple_mvvm.utils

typealias ResourceAction<T> = (T) -> Unit

class ResourceActions<T> {

    private val actions = mutableListOf<ResourceAction<T>>()

    var resource: T? = null
        set(newValue) {
            field = newValue
            if(newValue != null) {
                actions.forEach { it(newValue) }
                actions.clear()
            }
        }

    operator fun invoke(action: ResourceAction<T>) {
        val resource = this.resource
        if(resource != null) {
            action(resource)
        } else {
            actions += action
        }
    }

    fun clear() {
        actions.clear()
    }

}