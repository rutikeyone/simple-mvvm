package com.ru.foundation.sideeffects.resources.plugin

import android.content.Context
import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.SideEffectPlugin

class ResourcesPlugin : SideEffectPlugin<ResourcesSideEffectMediator, Nothing> {

    override val mediatorClass: Class<ResourcesSideEffectMediator>
        get() = ResourcesSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {
        return ResourcesSideEffectMediator(applicationContext)
    }

}