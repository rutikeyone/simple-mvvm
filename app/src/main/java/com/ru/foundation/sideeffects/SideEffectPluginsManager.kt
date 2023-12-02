package com.ru.foundation.sideeffects


class SideEffectPluginsManager {

    private val _plugins = mutableListOf<SideEffectPlugin<*, *>>()
    internal val plugins: List<SideEffectPlugin<*, *>>
        get() = _plugins

    fun <Mediator, Implementation> register(plugin: SideEffectPlugin<Mediator, Implementation>) {
        _plugins.add(plugin)
    }

}