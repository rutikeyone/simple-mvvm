package com.ru.foundation.navigator

import android.os.Bundle
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.ru.foundation.ARG_SCREEN
import com.ru.foundation.utils.Event
import com.ru.foundation.views.BaseFragment
import com.ru.foundation.views.BaseScreen
import com.ru.foundation.views.HasScreenTitle

typealias InitialScreenCreator = () -> BaseScreen

class StackFragmentNavigator(
    private val activity: AppCompatActivity,
    @IdRes private val containerId: Int,
    private val defaultTitle: String,
    private val animations: Animations,
    private val initialScreenCreator: InitialScreenCreator,
) : Navigator {

    private var result: Event<Any>? = null

    private val fragmentCallback = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            notifyScreenChanged()
            publishResult(f)
        }
    }

    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    override fun navigateBack(result: Any?) {
        if(result != null) {
            this.result = Event(result)
        }
        activity.onBackPressedDispatcher.onBackPressed()
    }

    private fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        fragment.arguments = bundleOf(ARG_SCREEN to screen)
        val transaction = activity.supportFragmentManager.beginTransaction()
        if(addToBackStack) transaction.addToBackStack(null)
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

    fun onCreate(savedInstanceState: Bundle?) {
        if(savedInstanceState == null) {
            launchFragment(
                screen = initialScreenCreator(),
                addToBackStack = false,
            )
        }
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallback, false)
    }

    fun onDestroy() {
        activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallback)
    }

    fun notifyScreenChanged() {
        val fragment = getCurrentFragment()
        val backStackEntryCount = activity.supportFragmentManager.backStackEntryCount
        if(backStackEntryCount > 0) {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if(fragment is HasScreenTitle && fragment.getScreenTitle() != null) {
            activity.supportActionBar?.title = fragment.getScreenTitle()
        } else {
            activity.supportActionBar?.title = defaultTitle
        }
    }

    fun onBackPressed() {
        val fragment = getCurrentFragment()
        fragment?.viewModel?.onBackPressed()
    }

    private fun publishResult(fragment: Fragment) {
        val result = result?.getValue() ?: return
        if(fragment is BaseFragment) {
            fragment.viewModel.onResult(result)
        }
    }

    private fun getCurrentFragment(): BaseFragment? {
        val fragment = activity.supportFragmentManager.findFragmentById(containerId)
        return if(fragment is BaseFragment) {
            fragment
        } else {
            null
        }
    }

    class Animations(
        @AnimRes val enterAnim: Int,
        @AnimRes val exitAnim: Int,
        @AnimRes val popEnterAnim: Int,
        @AnimRes val popExitAnim: Int,
    )
}