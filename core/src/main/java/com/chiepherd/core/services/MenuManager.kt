package com.chiepherd.core.services

import javafx.scene.layout.VBox

class MenuManager private constructor() {
    val menus = mutableMapOf<String, VBox>()

    fun get(key : String) : VBox { return menus[key] ?: VBox() }
    fun add(key : String, value : VBox) { menus[key] = value }

    private object Holder { val INSTANCE = MenuManager() }

    companion object {
        val instance: MenuManager by lazy { Holder.INSTANCE }

        fun get(key : String) : VBox { return instance.get(key) }
        fun add(key : String, value : VBox) { instance.add(key, value) }
    }
}
