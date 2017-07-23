package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.chiepherd.models.Project
import com.jfoenix.controls.JFXTextArea
import com.jfoenix.controls.JFXTextField
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.VBox
import org.json.JSONObject
import java.net.URL
import java.util.*

class EditProjectController(val project : Project) : ApplicationController() {
    @FXML lateinit var formProject : VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        loadProject(project)
    }

    fun loadProject(project: Project) {
        (formProject.children[1] as JFXTextField).text = project.name
        (formProject.children[2] as JFXTextField).text = project.label
        (formProject.children[3] as JFXTextArea).text = project.description
    }

    fun onCancel(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val root = (formProject.parent.parent as AnchorPane)
        val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/project/project_show.fxml"))
        fxmlLoader.setController(ProjectController(project.uuid!!, "Lead"))
        root.children.clear()
        root.children.add(fxmlLoader.load())
    }

    fun onUpdate(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val json =
                """{ "name": "${(formProject.children[1] as JFXTextField).text}",
                 "label": "${(formProject.children[2] as JFXTextField).text}",
                 "description": "${(formProject.children[3] as JFXTextArea).text}",
                 "uuid": "${project.uuid}" }"""
        val res = RabbitMQ.instance.sendMessage("chiepherd.project.update", json)

        println(res)
        // TODO: Check server errors
        if (JSONObject(res).has("uuid")) {
            Thread.sleep(1_000)
            switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/project/projects.fxml"))
        }
    }
}
