package com.ru.simple_mvvm.views.base

import androidx.fragment.app.Fragment
import com.ru.simple_mvvm.MainActivity

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    fun notifyScreenChanged() {
        val activity = requireActivity()
        if (activity is MainActivity) {
            activity.notifyScreenChanged()
        }
    }

}