package com.ru.foundation.views

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    fun notifyScreenChanged() {
        val activity = requireActivity()
        if (activity is FragmentHolder) {
            activity.notifyScreenChanged()
        }
    }

}