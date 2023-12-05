package com.ru.foundation.sideeffects.dialogs

import com.ru.foundation.sideeffects.dialogs.plugin.DialogConfig


interface Dialogs {

    suspend fun show(dialogConfig: DialogConfig): Boolean

}