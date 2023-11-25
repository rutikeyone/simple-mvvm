package com.ru.simple_mvvm

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.ru.simple_mvvm.views.HasScreenTitle
import com.ru.simple_mvvm.views.base.BaseFragment
import com.ru.simple_mvvm.views.base.BaseScreen
import com.ru.simple_mvvm.views.current_color.CurrentColorFragment

class MainActivity : AppCompatActivity() {
    private val activityViewModel by viewModels<MainViewModel> { ViewModelProvider.AndroidViewModelFactory(application) }

    private val fragmentCallback = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            notifyScreenChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            activityViewModel.launchFragment(
                activity = this,
                screen = CurrentColorFragment.Screen(),
                addToBackStack = false,
            )
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallback, false)
    }

    fun notifyScreenChanged() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if(backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        if(fragment is HasScreenTitle && fragment.getScreenTitle() != null) {
            supportActionBar?.title = fragment.getScreenTitle()
        } else {
            supportActionBar?.title = getString(R.string.app_name)
        }

        val result = activityViewModel.result.value?.getValue() ?: return
        if(fragment is BaseFragment) {
            fragment.viewModel.onResult(result)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.whenActivityActive.resource = this
    }

    override fun onPause() {
        super.onPause()
        activityViewModel.whenActivityActive.resource = null
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallback)
        super.onDestroy()
    }
}