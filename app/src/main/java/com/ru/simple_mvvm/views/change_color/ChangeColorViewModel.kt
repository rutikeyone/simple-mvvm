package com.ru.simple_mvvm.views.change_color

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import com.ru.foundation.model.ErrorResult
import com.ru.foundation.model.FinalResult
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.model.tasks.TaskFactory
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.model.colors.ColorsRepository
import com.ru.simple_mvvm.model.colors.NamedColor
import com.ru.foundation.navigator.Navigator
import com.ru.foundation.uiactions.UiActions
import com.ru.foundation.views.BaseViewModel
import com.ru.foundation.views.LiveResult
import com.ru.foundation.views.MediatorLiveResult
import com.ru.foundation.views.MutableLiveResult
import java.lang.IllegalStateException

class ChangeColorViewModel(
    private val colorsRepository: ColorsRepository,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val taskFactory: TaskFactory,
    screen: ChangeColorFragment.Screen,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    private val _availableColors = MutableLiveResult<List<NamedColor>>()
    private val _currentColorId = savedStateHandle.getLiveData(currentColorIdKey, screen.currentColorId)
    private val _saveInProgress = MutableLiveData(false)

    private val _viewState = MediatorLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState

    val screenTitle: LiveData<String> = _viewState.map { result ->
        if(result is SuccessResult) {
            val currentColor = result.data.colors.first { it.selected }
            uiActions.getString(R.string.change_color_screen_title, currentColor.namedColor)
        } else {
            uiActions.getString(R.string.change_color_screen_title_simple)
        }
    }

    init {
        load()
        _viewState.addSource(_availableColors) { mergeSources()  }
        _viewState.addSource(_currentColorId) { mergeSources() }
        _viewState.addSource(_saveInProgress) { mergeSources() }
    }

    fun onSavePressed() {
        _saveInProgress.postValue(true)
        taskFactory.async {
            val currentColorId = _currentColorId.value ?: throw IllegalStateException("Current color ID should be not NULL")
            val currentColor = colorsRepository.getById(currentColorId).await()
            colorsRepository.setCurrentColor(currentColor).await()
            return@async currentColor
        }.safeEnqueue(::onSaved)
    }

    private fun onSaved(result: FinalResult<NamedColor>) {
        _saveInProgress.value = false
        when (result) {
            is SuccessResult -> navigator.navigateBack(result.data)
            is ErrorResult -> uiActions.toast(uiActions.getString(R.string.error_happened))
        }
    }

    fun onCancelPressed() = navigator.navigateBack()

    private fun mergeSources() {
        val colors = _availableColors.value ?: return
        val currentColorId = _currentColorId.value ?: return
        val saveInProgress = _saveInProgress.value ?: return
        _viewState.value = colors.map { colorList ->
            ViewState(
                colors = colorList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress,
            )
        }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if(_saveInProgress.value == true) return
        _currentColorId.value = namedColor.id
    }

    fun tryAgain() = load()

    private fun load() = colorsRepository.getAvailableColor().into(_availableColors)

    companion object {
        const val currentColorIdKey = "currentColorId"
    }

    data class ViewState(
        val colors: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean,
    )
}