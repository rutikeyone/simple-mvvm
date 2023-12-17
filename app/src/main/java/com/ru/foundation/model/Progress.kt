package com.ru.foundation.model

sealed class Progress {

    fun isInProgress() = this !is EmptyProgress

    fun getPercentage() = (this as? PercentageProgress)?.value ?: PercentageProgress.START.value
}

data object EmptyProgress : Progress()

data class PercentageProgress(
    val value: Int,
): Progress() {

    companion object {
        val START = PercentageProgress(value = 0)
    }

}