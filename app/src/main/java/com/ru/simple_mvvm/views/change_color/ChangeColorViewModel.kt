package com.ru.simple_mvvm.views.change_color

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import com.ru.foundation.model.EmptyProgress
import com.ru.foundation.model.PendingResult
import com.ru.foundation.model.PercentageProgress
import com.ru.foundation.model.Progress
import com.ru.foundation.model.Result
import com.ru.foundation.model.SuccessResult
import com.ru.foundation.sideeffects.navigator.Navigator
import com.ru.foundation.sideeffects.resources.Resources
import com.ru.foundation.sideeffects.toasts.Toasts
import com.ru.foundation.utils.finiteShareIn
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.model.colors.ColorsRepository
import com.ru.simple_mvvm.model.colors.NamedColor
import com.ru.foundation.views.BaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.takeWhile
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
    private val _instanceSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)
    private val _sampledSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)

    val viewState: Flow<Result<ViewState>> = combine(
        _availableColors,
        _currentColorId,
        _instanceSaveInProgress,
        _sampledSaveInProgress,
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

    @OptIn(FlowPreview::class)
    fun onSavePressed() = viewModelScope.launch {
        _instanceSaveInProgress.emit(PercentageProgress.START)
        _sampledSaveInProgress.emit(PercentageProgress.START)
        try {
            val currentColorId = _currentColorId.value
            val currentColor = colorsRepository.getById(currentColorId)

            val flow = colorsRepository.setCurrentColor(currentColor)
                .finiteShareIn(this)

            val instanceJob = async {
                flow.collect { percentage ->
                    _instanceSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            val sampledSaveInProgress = async {
                flow.sample(200).collect { percentage ->
                    _sampledSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            instanceJob.await()
            sampledSaveInProgress.await()

            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if(e !is CancellationException) {
                toast.toast(resources.getString(R.string.error_happened))
            }
        } finally {
            _instanceSaveInProgress.emit(EmptyProgress)
            _sampledSaveInProgress.emit(EmptyProgress)
        }
    }

    fun onCancelPressed() = navigator.goBack()

    private fun mergeSources(
        colors: Result<List<NamedColor>>,
        currentColorId: Long,
        instanceSaveInProgress: Progress,
        sampledSaveInProgress: Progress,
    ): Result<ViewState> {
        return colors.map { colorList ->
            ViewState(
                colors = colorList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !instanceSaveInProgress.isInProgress(),
                showCancelButton = !instanceSaveInProgress.isInProgress(),
                showSaveProgressBar = instanceSaveInProgress.isInProgress(),
                saveProgressPercentage = instanceSaveInProgress.getPercentage(),
                saveProgressPercentageMessage = resources.getString(R.string.percentage_value, sampledSaveInProgress.getPercentage())
            )
        }
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if(_instanceSaveInProgress.value.isInProgress()) return
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
        val saveProgressPercentage: Int,
        val saveProgressPercentageMessage: String,
    )
}