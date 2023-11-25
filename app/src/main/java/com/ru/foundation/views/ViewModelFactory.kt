package com.ru.foundation.views

import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.ru.foundation.ARG_SCREEN
import com.ru.foundation.BaseApplication
import java.lang.IllegalStateException
import java.lang.reflect.Constructor


inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val application = requireActivity().application
    val applicationException = IllegalStateException("Application must implementation interface BaseApplication")
    val baseApplication = if(application is BaseApplication) application else throw applicationException
    val activity = requireActivity()
    val fragmentHolderException = IllegalStateException("Activity must implementation interface FragmentHolder")
    val fragmentHolder = if(activity is FragmentHolder) activity else throw fragmentHolderException
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen
    val activityScopeViewModel = fragmentHolder.getActivityScopeViewModel()
    val dependencies = baseApplication.repositoryDependencies + listOf(screen, activityScopeViewModel)
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