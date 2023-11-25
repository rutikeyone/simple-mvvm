package com.ru.simple_mvvm.views.change_color

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.model.colors.ColorsRepository
import com.ru.simple_mvvm.model.colors.NamedColor
import com.ru.foundation.navigator.Navigator
import com.ru.foundation.uiactions.UiActions
import com.ru.foundation.views.BaseViewModel

class ChangeColorViewModel(
    private val colorsRepository: ColorsRepository,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    screen: ChangeColorFragment.Screen,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    private val _availableColors = MutableLiveData<List<NamedColor>>()
    val availableColors: LiveData<List<NamedColor>> = _availableColors

    private val _currentColorId = savedStateHandle.getLiveData(currentColorIdKey, screen.currentColorId)
    val currentColorId: LiveData<Long> = _currentColorId

    private val _colorList = MediatorLiveData<List<NamedColorListItem>>()
    val colorList: LiveData<List<NamedColorListItem>> = _colorList

    private val _screenTitle = MutableLiveData<String>()
    val screenTitle: LiveData<String> = _screenTitle

    init {
        _availableColors.value = colorsRepository.getAvailableColor()
        _colorList.addSource(_availableColors) { mergeSources()  }
        _colorList.addSource(_currentColorId) { mergeSources() }
    }

    fun onSavePressed() {
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colorsRepository.getById(currentColorId)
        colorsRepository.currentColor = currentColor
        navigator.navigateBack(currentColor)
    }

    fun onCancelPressed() = navigator.navigateBack()

    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val currentColor = colors.first { it.id == currentColorId }
        _colorList.value = colors.map { NamedColorListItem(it, currentColorId == it.id) }
        _screenTitle.value = uiActions.getString(R.string.change_color_screen_title, currentColor.name)
    }

    companion object {
        const val currentColorIdKey = "currentColorId"
    }

    override fun onColorChosen(namedColor: NamedColor) {
        _currentColorId.value = namedColor.id
    }
}