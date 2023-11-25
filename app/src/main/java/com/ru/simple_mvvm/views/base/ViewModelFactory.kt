package com.ru.simple_mvvm.views.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.ru.simple_mvvm.ARG_SCREEN
import com.ru.simple_mvvm.App
import com.ru.simple_mvvm.MainViewModel
import java.lang.reflect.Constructor


inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val application = requireActivity().application as App
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen
    val provider = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory(application))
    val mainViewModel = provider[MainViewModel::class.java]
    val dependencies = application.models + listOf(screen, mainViewModel)
    ViewModelFactory(dependencies, this)
}

class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner,
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val constructors = modelClass.constructors
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!
        val dependenciesWithSavedStateHandle = dependencies + handle
        val dependencies = findDependencies(constructor, dependenciesWithSavedStateHandle)
        return constructor.newInstance(*dependencies.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>): List<Any> {
        val arguments = mutableListOf<Any>()
        constructor.parameterTypes.forEach { parameterClass ->
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            arguments.add(dependency)
        }
        return arguments
    }

}