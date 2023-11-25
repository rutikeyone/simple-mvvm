package com.ru.simple_mvvm

import android.app.Application
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ru.simple_mvvm.utils.Event
import com.ru.simple_mvvm.utils.ResourceActions
import com.ru.simple_mvvm.views.Navigator
import com.ru.simple_mvvm.views.UIActions
import com.ru.simple_mvvm.views.base.BaseScreen
import com.ru.simple_mvvm.views.base.LiveEvent
import com.ru.simple_mvvm.views.base.MutableLiveEvent

const val ARG_SCREEN = "ARG_SCREEN"

class MainViewModel(
    application: Application,
) : AndroidViewModel(application), Navigator, UIActions {

    val whenActivityActive = ResourceActions<MainActivity>()

    private val _result = MutableLiveEvent<Any>()
    val result: LiveEvent<Any> = _result

    override fun launch(screen: BaseScreen) = whenActivityActive {
        launchFragment(it, screen)
    }

    override fun navigateBack(result: Any?) = whenActivityActive {
        if(result != null) {
            _result.value = Event(result)
        }
        it.onBackPressedDispatcher.onBackPressed()
    }

    override fun toast(message: String) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }

    override fun getString(@StringRes messageRes: Int, vararg args: Any): String {
        return getApplication<App>().getString(messageRes, *args)
    }

    fun launchFragment(activity: MainActivity, screen: BaseScreen, addToBackStack: Boolean = true) {
        val fragment = screen.javaClass.enclosingClass.newInstance() as Fragment
        fragment.arguments = bundleOf(ARG_SCREEN to screen)
        val transaction = activity.supportFragmentManager.beginTransaction()
        if(addToBackStack) transaction.addToBackStack(null)
        transaction
            .setCustomAnimations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit,
            )
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onCleared() {
        super.onCleared()
        whenActivityActive.clear()
    }
}