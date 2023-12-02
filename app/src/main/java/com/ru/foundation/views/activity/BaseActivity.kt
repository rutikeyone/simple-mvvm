package com.ru.foundation.views.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ru.foundation.sideeffects.SideEffectPluginsManager

abstract class BaseActivity : AppCompatActivity(), ActivityDelegateHolder {

    private var _delegate: ActivityDelegate? = null
    override val delegate: ActivityDelegate
        get() = _delegate!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _delegate = ActivityDelegate(this).also {
            registerPlugins(it.sideEffectPluginsManager)
            it.onCreate(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        delegate.onSavedInstanceState(outState)
    }

    override fun onBackPressed() {
        if (!delegate.onBackPressed()) super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        _delegate = null
    }

    override fun onSupportNavigateUp(): Boolean {
        return delegate.onSupportNavigateUp() ?: super.onSupportNavigateUp()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        delegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        delegate.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    abstract fun registerPlugins(manager: SideEffectPluginsManager)

}