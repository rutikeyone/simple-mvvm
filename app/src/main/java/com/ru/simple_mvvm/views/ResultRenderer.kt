package com.ru.simple_mvvm.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import com.ru.foundation.model.Result
import com.ru.foundation.views.BaseFragment
import com.ru.simple_mvvm.R
import com.ru.simple_mvvm.databinding.PartResultBinding

fun <T> BaseFragment.renderSimpleResult(
    root: ViewGroup,
    result: Result<T>,
    onSuccessResult: (T) -> Unit,
) {
    val binding = PartResultBinding.bind(root)

    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id!= R.id.errorContainer }
                .forEach { it.visibility = View.VISIBLE }
            onSuccessResult(successData)
        }
    )
}

fun BaseFragment.onTryAgain(
    root: View,
    onTryAgainPressed: () -> Unit,
) {
    root.findViewById<Button>(R.id.tryAgainButton).setOnClickListener { onTryAgainPressed() }
}