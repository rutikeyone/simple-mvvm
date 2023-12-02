package com.ru.foundation.sideeffects.navigator.plugin

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.ru.foundation.sideeffects.SideEffectImplementation
import com.ru.foundation.sideeffects.navigator.Navigator
import com.ru.foundation.utils.Event
import com.ru.foundation.views.BaseFragment
import com.ru.foundation.views.BaseScreen
import com.ru.foundation.views.BaseScreen.Companion.ARG_SCREEN
import com.ru.foundation.views.HasScreenTitle

class StackFragmentNavigator(
    @IdRes private val containerId: Int,
    private val defaultTitle: String,
    private val animations: Animations,
    private val initialScreenCreator: () -> BaseScreen
) : SideEffectImplementation(), Navigator, LifecycleObserver {

    private var result: Event<Any>? = null

    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    override fun goBack(result: Any?) {
        if (result != null) {
            this.result = Event(result)
        }
        requireActivity().onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().lifecycle.addObserver(this)
        if (savedInstanceState == null) {
            launchFragment(
                screen = initialScreenCreator(),
                addToBackStack = false
            )
        }
        requireActivity().supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        requireActivity().supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
    }

    override fun onBackPressed(): Boolean {
        val f = getCurrentFragment()
        return if (f is BaseFragment) {
            f.viewModel.onBackPressed()
        } else {
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        requireActivity().onBackPressed()
        return true
    }

    override fun onRequestUpdates() {
        val f = getCurrentFragment()

        if (requireActivity().supportFragmentManager.backStackEntryCount > 0) {
            requireActivity().supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            requireActivity().supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if (f is HasScreenTitle && f.getScreenTitle() != null) {
            requireActivity().supportActionBar?.title = f.getScreenTitle()
        } else {
            requireActivity().supportActionBar?.title = defaultTitle
        }
    }

    private fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        fragment.arguments = bundleOf(ARG_SCREEN to screen)

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .setCustomAnimations(
                animations.enterAnim,
                animations.exitAnim,
                animations.popEnterAnim,
                animations.popExitAnim,
            )
            .replace(containerId, fragment)
            .commit()
    }

    private fun publishResults(fragment: Fragment) {
        val result = result?.getValue() ?: return
        if (fragment is BaseFragment) {
            fragment.viewModel.onResult(result)
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return requireActivity().supportFragmentManager.findFragmentById(containerId)
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            onRequestUpdates()
            publishResults(f)
        }
    }

    class Animations(
        @AnimRes val enterAnim: Int,
        @AnimRes val exitAnim: Int,
        @AnimRes val popEnterAnim: Int,
        @AnimRes val popExitAnim: Int,
    )
}