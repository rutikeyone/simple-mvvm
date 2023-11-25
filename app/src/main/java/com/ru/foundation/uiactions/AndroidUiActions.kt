package com.ru.foundation.uiactions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

class AndroidUiActions(
    private val applicationContext: Context,
) : UiActions {

    override fun toast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    override fun getString(@StringRes messageRes: Int, vararg args: Any): String {
        return applicationContext.getString(messageRes, *args)
    }

}