package com.ru.foundation.sideeffects.permissions.plugin

import android.content.Context
import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.SideEffectPlugin


class PermissionsPlugin :
    SideEffectPlugin<PermissionsSideEffectMediator, PermissionsSideEffectImpl> {

    override val mediatorClass: Class<PermissionsSideEffectMediator>
        get() = PermissionsSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<PermissionsSideEffectImpl> {
        return PermissionsSideEffectMediator(applicationContext)
    }

    override fun createImplementation(mediator: PermissionsSideEffectMediator): PermissionsSideEffectImpl {
        return PermissionsSideEffectImpl(mediator.retainedState)
    }

}