package com.chiepherd.app.plugins


class PluginList(val plugins: MutableList<Plugin> = mutableListOf<PluginList.Plugin>()) {
    data class Plugin(val path : String, val fxml : String, val name : String, val is_internal : Boolean)

    fun addPlugin(plugin : Plugin) { plugins.add(plugin) }
}
