package com.chiepherd.core.controllers


import com.chiepherd.core.extensions.javafx.event.stage
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import com.chiepherd.core.services.MenuManager
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import khttp.responses.Response
import java.net.URL

abstract class ApplicationController : Initializable {
    val homeParent : VBox by lazy {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/layouts/application.fxml")

        loader.load<VBox>()
    }

    protected fun sidePane(event: ActionEvent) : AnchorPane {
        return (event.stage.scene.lookup("SplitPane") as SplitPane).items[0] as AnchorPane
    }

    protected fun yieldPane(event: ActionEvent) : AnchorPane {
        return (event.stage.scene.lookup("SplitPane") as SplitPane).items[1] as AnchorPane
    }

    private fun newScene(path : URL) : BorderPane {
        return FXMLLoader.load<BorderPane>(path)
    }

    protected fun switchScene(actionEvent : ActionEvent, resource : URL, menu : VBox = MenuManager.get("Logged")) {
        val sidePane = sidePane(actionEvent)
        sidePane.children.clear()
        sidePane.children.add(menu)

        val yieldPane = yieldPane(actionEvent)
        yieldPane.children.clear()
        yieldPane.children.add(newScene(resource))
    }

    protected fun displayError(res: Response) {
        println(res.text)
    }
}
