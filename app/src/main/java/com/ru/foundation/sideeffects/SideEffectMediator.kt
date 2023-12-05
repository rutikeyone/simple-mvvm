package com.ru.foundation.sideeffects

import com.ru.foundation.model.dispatchers.Dispatcher
import com.ru.foundation.model.dispatchers.MainThreadDispatcher
import ua.cn.stu.foundation.utils.ResourceActions

    open class SideEffectMediator<Implementation>(
    dispatcher: Dispatcher = MainThreadDispatcher()
) {

    protected val target = ResourceActions<Implementation>(dispatcher)

    fun setTarget(target: Implementation?) {
        this.target.resource = target
    }

    fun clear() {
        target.clear()
    }

}