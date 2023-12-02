package com.ru.foundation.sideeffects.permissions

import com.ru.foundation.model.tasks.Task
import com.ru.foundation.sideeffects.permissions.plugin.PermissionStatus
import com.ru.foundation.sideeffects.permissions.plugin.PermissionsPlugin

interface Permissions {

    fun hasPermissions(permission: String): Boolean

    fun requestPermission(permission: String): Task<PermissionStatus>

}