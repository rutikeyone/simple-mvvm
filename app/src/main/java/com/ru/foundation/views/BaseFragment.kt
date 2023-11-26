package com.ru.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.ru.foundation.model.ErrorResult
import com.ru.foundation.model.PendingResult
import java.lang.Exception
import com.ru.foundation.model.Result
import com.ru.foundation.model.SuccessResult

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    fun notifyScreenChanged() {
        val activity = requireActivity()
        if (activity is FragmentHolder) {
            activity.notifyScreenChanged()
        }
    }

    fun <T> renderResult(
        root: ViewGroup,
        result: Result<T>,
        onPending: () -> Unit,
        onError: (Exception) -> Unit,
        onSuccess: (T) -> Unit,
    ) {
        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }
    }

}