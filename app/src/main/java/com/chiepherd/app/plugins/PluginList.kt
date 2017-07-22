package com.chiepherd.app.plugins

import com.chiepherd.core.services.RabbitMQ


class PluginList private constructor(val plugins: MutableList<Plugin> = mutableListOf<PluginList.Plugin>()) {
    data class Plugin(val classLoader : ClassLoader, val fxml : String, val name : String, val is_internal : Boolean, val klass : String)

    fun add(plugin : Plugin) { plugins.add(plugin) }

    private object Holder { val INSTANCE = PluginList() }

    companion object {
        val instance: PluginList by lazy { Holder.INSTANCE }
    }
}
