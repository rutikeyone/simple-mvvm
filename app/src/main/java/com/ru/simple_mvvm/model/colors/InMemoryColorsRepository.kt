package com.ru.simple_mvvm.model.colors

import android.graphics.Color
import com.ru.foundation.model.coroutines.CoroutineDispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class InMemoryColorsRepository(
    private val ioDispatcher: CoroutineDispatchers.IODispatcher,
): ColorsRepository {

    private var currentColor = AVAILABLE_COLORS[0]

    override suspend fun getAvailableColor(): List<NamedColor> = withContext(ioDispatcher.value) {
        delay(1000)
        return@withContext AVAILABLE_COLORS
    }

    private val currentColorFLow = MutableSharedFlow<NamedColor>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    override fun listenCurrentColor(): Flow<NamedColor> = currentColorFLow

    override suspend fun getById(id: Long): NamedColor = withContext(ioDispatcher.value) {
        delay(1000)
        return@withContext AVAILABLE_COLORS.first { it.id == id }
    }

    override fun setCurrentColor(color: NamedColor): Flow<Int> = flow {
        if(currentColor != color) {
            var progress = 0
            while(progress < 100) {
                progress += 2
                delay(30)
                emit(progress)
            }
            currentColor = color
            currentColorFLow.emit(currentColor)
        } else {
            emit(100)
        }
    }.flowOn(ioDispatcher.value)

    override suspend fun getCurrentColor(): NamedColor = withContext(ioDispatcher.value) {
        delay(100)
        return@withContext currentColor
    }

    companion object {
        private val AVAILABLE_COLORS = listOf(
            NamedColor(1, "Red", Color.RED),
            NamedColor(2, "Green", Color.GREEN),
            NamedColor(3, "Blue", Color.BLUE),
            NamedColor(4, "Yellow", Color.YELLOW),
            NamedColor(5, "Magenta", Color.MAGENTA),
            NamedColor(6, "Cyan", Color.CYAN),
            NamedColor(7, "Gray", Color.GRAY),
            NamedColor(8, "Navy", Color.rgb(0, 0, 128)),
            NamedColor(9, "Pink", Color.rgb(255, 20, 147)),
            NamedColor(10, "Sienna", Color.rgb(160, 82, 45)),
            NamedColor(11, "Khaki", Color.rgb(240, 230, 140)),
            NamedColor(12, "Forest Green", Color.rgb(34, 139, 34)),
            NamedColor(13, "Sky", Color.rgb(135, 206, 250)),
            NamedColor(14, "Olive", Color.rgb(107, 142, 35)),
            NamedColor(15, "Violet", Color.rgb(148, 0, 211)),
        )
    }
}