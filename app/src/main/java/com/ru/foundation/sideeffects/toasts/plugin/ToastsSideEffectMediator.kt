package com.ru.foundation.sideeffects.toasts.plugin

import android.content.Context
import android.widget.Toast
import com.ru.foundation.model.dispatchers.MainThreadDispatcher
import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.toasts.Toasts

class ToastsSideEffectMediator(
    private val applicationContext: Context
) : SideEffectMediator<Nothing>(), Toasts {

    private val dispatcher = MainThreadDispatcher()

    override fun toast(message: String) {
        dispatcher.dispatch {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}