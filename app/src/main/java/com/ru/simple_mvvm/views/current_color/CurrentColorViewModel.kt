package com.ru.simple_mvvm.views.current_color

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.model.colors.ColorListener
import com.ru.simple_mvvm.model.colors.ColorsRepository
import com.ru.simple_mvvm.model.colors.NamedColor
import com.ru.simple_mvvm.views.Navigator
import com.ru.simple_mvvm.views.UIActions
import com.ru.simple_mvvm.views.base.BaseViewModel
import com.ru.simple_mvvm.views.change_color.ChangeColorFragment

class CurrentColorViewModel(
    private val navigator: Navigator,
    private val uiActions: UIActions,
    private val colorsRepository: ColorsRepository,
) : BaseViewModel() {

    private val _currentColor = MutableLiveData<NamedColor>()
    val currentColor: LiveData<NamedColor> = _currentColor

    private val colorListener: ColorListener = {
        _currentColor.postValue(it)
    }

    init {
        colorsRepository.addListener(colorListener)
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
        val currentColor = _currentColor.value ?: return
        val screen = ChangeColorFragment.Screen(currentColor.id)
        navigator.launch(screen)
    }

}