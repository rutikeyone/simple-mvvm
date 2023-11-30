package com.ru.simple_mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.AnimRes
import com.ru.foundation.ActivityScopeViewModel
import com.ru.foundation.navigator.IntermediateNavigator
import com.ru.foundation.navigator.StackFragmentNavigator
import com.ru.foundation.uiactions.AndroidUiActions
import com.ru.foundation.utils.viewModelCreator
import com.ru.foundation.views.FragmentHolder
import com.ru.simple_mvvm.views.current_color.CurrentColorFragment

class MainActivity : AppCompatActivity(), FragmentHolder {
    private lateinit var navigator: StackFragmentNavigator

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiActions = AndroidUiActions(applicationContext),
            navigator = IntermediateNavigator(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            defaultTitle = getString(R.string.app_name),
            animations = StackFragmentNavigator.Animations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit,
            ),
            initialScreenCreator = { CurrentColorFragment.Screen() }
        )
        navigator.onCreate(savedInstanceState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onResume() {
        activityViewModel.navigator.setTarget(navigator)
        super.onResume()
    }

    override fun onPause() {
        activityViewModel.navigator.setTarget(null)
        super.onPause()
    }

    override fun onBackPressed() {
        navigator.onBackPressed()
        super.onBackPressed()
    }

    override fun onDestroy() {
        navigator.onDestroy()
        super.onDestroy()
    }

    override fun notifyScreenChanged() = navigator.notifyScreenChanged()

    override fun getActivityScopeViewModel(): ActivityScopeViewModel = activityViewModel

}