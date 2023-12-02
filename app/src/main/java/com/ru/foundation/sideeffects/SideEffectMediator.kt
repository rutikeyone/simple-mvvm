package com.ru.foundation.sideeffects

import com.ru.foundation.model.tasks.dispatchers.Dispatcher
import com.ru.foundation.model.tasks.dispatchers.MainThreadDispatcher
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