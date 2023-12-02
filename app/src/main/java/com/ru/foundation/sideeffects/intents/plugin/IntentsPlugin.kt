package com.ru.foundation.sideeffects.intents.plugin

import android.content.Context
import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.SideEffectPlugin
import com.ru.foundation.sideeffects.intents.Intents

class IntentsPlugin : SideEffectPlugin<Intents, Nothing> {

    override val mediatorClass: Class<Intents>
        get() = Intents::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {
        return IntentsSideEffectMediator(applicationContext)
    }

}