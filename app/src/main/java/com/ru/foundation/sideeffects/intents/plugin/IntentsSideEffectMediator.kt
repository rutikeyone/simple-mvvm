package com.ru.foundation.sideeffects.intents.plugin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.ru.foundation.sideeffects.SideEffectMediator
import com.ru.foundation.sideeffects.intents.Intents

class IntentsSideEffectMediator(
    private val applicationContext: Context
) : SideEffectMediator<Nothing>(), Intents {

    override fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", applicationContext.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (applicationContext.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            applicationContext.startActivity(intent)
        }
    }

}