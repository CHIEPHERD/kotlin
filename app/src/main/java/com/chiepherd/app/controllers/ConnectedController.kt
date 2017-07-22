package com.chiepherd.app.controllers

import com.chiepherd.app.plugins.PluginList
import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXTextField
import javafx.event.ActionEvent
import javafx.fxml.FXML
import java.net.URL
import java.util.*
import com.chiepherd.core.controllers.ApplicationController
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.Pane

class ConnectedController : ApplicationController() {
    @FXML lateinit var rootContainer : BorderPane
    @FXML lateinit var pluginContainer : Pane

    val gPane : GridPane = GridPane()

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("VIEW CONTROLLER \n")
    }

    @FXML fun onUpdate(actionEvent : ActionEvent?) {
        println("Add Action")

        val buttons = PluginList.instance.plugins.map { plugin ->
            val button = JFXButton(plugin.name)
            button.onAction = EventHandler {
                println("FXML => ${plugin.fxml}")
                println(plugin.name)
                println(plugin.classLoader)
                println(plugin.classLoader.getResource(plugin.fxml))
                println(actionEvent)
                switchScene(actionEvent!!, plugin.classLoader.getResource(plugin.fxml))
            }
            button
        }

        println(buttons)
        pluginContainer.children.add(gPane)

        println("Add child to pane")
        gPane.children.addAll(buttons)
    }
}
