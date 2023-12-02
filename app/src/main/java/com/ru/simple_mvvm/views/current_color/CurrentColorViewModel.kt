package com.ru.simple_mvvm.views.current_color

import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.model.takeSuccess
import com.ru.foundation.model.tasks.dispatchers.Dispatcher
import com.ru.foundation.model.tasks.factories.TasksFactory
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

class CurrentColorViewModel(
    private val colorsRepository: ColorsRepository,
    private val tasksFactory: TasksFactory,
    private val dialogs: Dialogs,
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val permissions: Permissions,
    private val intents: Intents,
    dispatcher: Dispatcher,
) : BaseViewModel(dispatcher) {

    private val _currentColor = MutableLiveResult<NamedColor>(PendingResult())
    val currentColor: LiveResult<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        val successResult = SuccessResult(it)
        _currentColor.postValue(successResult)
    }

    init {
        colorsRepository.addListener(colorListener)
        load()
    }

    override fun onCleared() {
        super.onCleared()
        colorsRepository.removeListener(colorListener)
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

    private fun load() {
        colorsRepository.getCurrentColor().into(_currentColor)
    }

    fun requestPermission() = tasksFactory.async<Unit> {
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = permissions.hasPermissions(permission)
        if (hasPermission) {
            dialogs.show(createPermissionAlreadyGrantedDialog()).await()
        } else {
            when (permissions.requestPermission(permission).await()) {
                PermissionStatus.GRANTED -> {
                    toasts.toast(resources.getString(R.string.permissions_grated))
                }
                PermissionStatus.DENIED -> {
                    toasts.toast(resources.getString(R.string.permissions_denied))
                }
                PermissionStatus.DENIED_FOREVER -> {
                    if (dialogs.show(createAskForLaunchingAppSettingsDialog()).await()) {
                        intents.openAppSettings()
                    }
                }
            }
        }
    }.safeEnqueue()

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