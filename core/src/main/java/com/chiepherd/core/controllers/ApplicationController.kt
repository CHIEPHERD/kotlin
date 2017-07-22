package com.chiepherd.core.controllers


import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import com.chiepherd.core.extensions.javafx.event.stage
import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import khttp.responses.Response
import netscape.javascript.JSObject.getWindow
import javafx.stage.Stage
import java.net.URL


abstract class ApplicationController : Initializable {
    val homeParent : VBox by lazy {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/layouts/Application.fxml")

        loader.load<VBox>()
    }

    private fun contentLayout() : AnchorPane {
        return (homeParent.lookup("SplitPane") as SplitPane).items[1] as AnchorPane
    }

    private fun newScene(path : URL) : BorderPane {
        return FXMLLoader.load<BorderPane>(path)
    }

    private fun homeScene() : Scene {
        return Scene(homeParent)
    }

    protected fun switchScene(actionEvent : ActionEvent, resource : URL) {
        println("resource $resource")
        println("actionEvent $actionEvent")
        val homeStage = actionEvent.stage
        homeStage.scene = homeScene()
        homeStage.show()

        println(newScene(resource))

        contentLayout().children.add(newScene(resource))
    }

    protected fun displayError(res: Response) {
        println(res.text)
    }
}
