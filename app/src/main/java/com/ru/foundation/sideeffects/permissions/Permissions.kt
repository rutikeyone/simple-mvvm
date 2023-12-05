package com.ru.foundation.sideeffects.permissions

import com.ru.foundation.sideeffects.permissions.plugin.PermissionStatus

interface Permissions {

    fun hasPermissions(permission: String): Boolean

    suspend fun requestPermission(permission: String): PermissionStatus

}