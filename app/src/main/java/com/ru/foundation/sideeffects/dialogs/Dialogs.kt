package com.ru.foundation.sideeffects.dialogs

import com.ru.foundation.model.tasks.Task
import com.ru.foundation.sideeffects.dialogs.plugin.DialogConfig
import com.ru.foundation.sideeffects.dialogs.plugin.DialogsPlugin


interface Dialogs {

    fun show(dialogConfig: DialogConfig): Task<Boolean>

}