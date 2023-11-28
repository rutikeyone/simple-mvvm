package com.ru.simple_mvvm.views.current_color

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ru.foundation.model.ErrorResult
import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.model.takeSuccess
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.model.colors.ColorListener
import com.ru.simple_mvvm.model.colors.ColorsRepository
import com.ru.simple_mvvm.model.colors.NamedColor
import com.ru.foundation.navigator.Navigator
import com.ru.foundation.uiactions.UiActions
import com.ru.foundation.views.BaseViewModel
import com.ru.foundation.views.LiveResult
import com.ru.foundation.views.MutableLiveResult
import com.ru.simple_mvvm.views.change_color.ChangeColorFragment
import java.lang.RuntimeException

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val colorsRepository: ColorsRepository,
) : BaseViewModel() {

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
            val message = uiActions.getString(R.string.changed_color, result.name)
            uiActions.toast(message)
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
}