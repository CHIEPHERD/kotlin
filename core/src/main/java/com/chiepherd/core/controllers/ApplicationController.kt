package com.chiepherd.core.controllers


import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import com.chiepherd.core.extensions.javafx.event.stage
import javafx.event.ActionEvent
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import khttp.responses.Response

abstract class ApplicationController : Initializable {
    val homeParent : VBox by lazy {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/layouts/Application.fxml")

        loader.load<VBox>()
    }

    private fun contentLayout() : AnchorPane {
        return homeParent.lookup("#Content") as AnchorPane
    }

    private fun newScene(path : String) : BorderPane {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource(path)
        return loader.load<BorderPane>()
    }

    private fun homeScene() : Scene {
        return Scene(homeParent)
    }

    protected fun switchScene(actionEvent : ActionEvent, pathNewScene : String) {
        println(pathNewScene)
        val homeStage = actionEvent.stage
        homeStage.scene = homeScene()
        homeStage.show()

        println(newScene(pathNewScene))

        contentLayout().children.add(newScene(pathNewScene))
    }

    protected fun displayError(res: Response) {
        println(res.text)
    }
}
