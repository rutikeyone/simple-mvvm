package com.ru.simple_mvvm.views.current_color

import androidx.lifecycle.viewModelScope
import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.model.takeSuccess
import com.ru.foundation.sideeffects.dialogs.Dialogs
import com.ru.foundation.sideeffects.dialogs.plugin.DialogConfig
import com.ru.foundation.sideeffects.intents.Intents
import com.ru.foundation.sideeffects.navigator.Navigator
import com.ru.foundation.sideeffects.permissions.Permissions
import com.ru.foundation.sideeffects.permissions.plugin.PermissionStatus
import com.ru.foundation.sideeffects.resources.Resources
import com.ru.foundation.sideeffects.toasts.Toasts
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.model.colors.ColorListener
import com.ru.simple_mvvm.model.colors.ColorsRepository
import com.ru.simple_mvvm.model.colors.NamedColor
import com.ru.foundation.views.BaseViewModel
import com.ru.foundation.views.LiveResult
import com.ru.foundation.views.MutableLiveResult
import com.ru.simple_mvvm.views.change_color.ChangeColorFragment
import kotlinx.coroutines.launch

class CurrentColorViewModel(
    private val colorsRepository: ColorsRepository,
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val permissions: Permissions,
    private val intents: Intents,
    private val dialogs: Dialogs,
) : BaseViewModel() {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    init {
        viewModelScope.launch {
            colorsRepository.listenCurrentColor().collect {
                _currentColor.value = SuccessResult(it)
            }
        }
        load()
    }

    override fun onResult(result: Any) {
        super.onResult(result)
        if(result is NamedColor) {
            val message = resources.getString(R.string.changed_color, result.name)
            toasts.toast(message)
        }
    }

    fun changeColor() {
        val currentColor = _currentColor.value?.takeSuccess() ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

    fun tryAgain() = load()

    private fun load() = into(_currentColor) {
        colorsRepository.getCurrentColor()
    }

    fun requestPermission() = viewModelScope.launch {
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = permissions.hasPermissions(permission)
        if (hasPermission) {
            dialogs.show(createPermissionAlreadyGrantedDialog())
        } else {
            when (permissions.requestPermission(permission)) {
                PermissionStatus.GRANTED -> {
                    toasts.toast(resources.getString(R.string.permissions_grated))
                }
                PermissionStatus.DENIED -> {
                    toasts.toast(resources.getString(R.string.permissions_denied))
                }
                PermissionStatus.DENIED_FOREVER -> {
                    if (dialogs.show(createAskForLaunchingAppSettingsDialog())) {
                        intents.openAppSettings()
                    }
                }
            }
        }
    }

    private fun createPermissionAlreadyGrantedDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.permissions_already_granted),
        positiveButton = resources.getString(R.string.action_ok)
    )

    private fun createAskForLaunchingAppSettingsDialog() = DialogConfig(
        title = resources.getString(R.string.dialog_permissions_title),
        message = resources.getString(R.string.open_app_settings_message),
        positiveButton = resources.getString(R.string.action_open),
        negativeButton = resources.getString(R.string.action_cancel)
    )

}