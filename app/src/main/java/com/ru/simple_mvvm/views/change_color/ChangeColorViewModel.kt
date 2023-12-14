package com.ru.simple_mvvm.views.change_color

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.Result
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.sideeffects.navigator.Navigator
import com.ru.foundation.sideeffects.resources.Resources
import com.ru.foundation.sideeffects.toasts.Toasts
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.model.colors.ColorsRepository
import com.ru.simple_mvvm.model.colors.NamedColor
import com.ru.foundation.views.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.cancellation.CancellationException

class ChangeColorViewModel(
    private val navigator: Navigator,
    private val toast: Toasts,
    private val resources: Resources,
    private val colorsRepository: ColorsRepository,
    screen: ChangeColorFragment.Screen,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    private val _availableColors = MutableStateFlow<Result<List<NamedColor>>>(PendingResult())
    private val _currentColorId = savedStateHandle.getMutableStateFlow(currentColorIdKey, screen.currentColorId)
    private val _saveInProgress = MutableStateFlow(false)

    val viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _saveInProgress,
        ::mergeSources,
    )

    val screenTitle: LiveData<String> = viewState.map { result ->
        return@map if(result is SuccessResult) {
            val currentColor = result.data.colors.first { it.selected }
            resources.getString(R.string.change_color_screen_title, currentColor.namedColor)
        } else {
            resources.getString(R.string.change_color_screen_title_simple)
        }
    }.asLiveData()

    init {
        load()
    }

    fun onSavePressed() = viewModelScope.launch {
        _saveInProgress.emit(true)
        try {
            val currentColorId = _currentColorId.value
            val currentColor = colorsRepository.getById(currentColorId)
            colorsRepository.setCurrentColor(currentColor).collect()
            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if(e !is CancellationException) {
                toast.toast(resources.getString(R.string.error_happened))
            }
        } finally {
            _saveInProgress.emit(false)
        }
    }

    fun onCancelPressed() = navigator.goBack()

    private fun mergeSources(
        colors: Result<List<NamedColor>>,
        currentColorId: Long,
        saveInProgress: Boolean,
    ): Result<ViewState> {
        return colors.map { colorList ->
            ViewState(
                colors = colorList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !saveInProgress,
                showCancelButton = !saveInProgress,
                showSaveProgressBar = saveInProgress,
            )
        }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if(_saveInProgress.value) return
        _currentColorId.value = namedColor.id
    }

    fun tryAgain() = load()

    private fun load() = into(_availableColors) {
        colorsRepository.getAvailableColor()
    }

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