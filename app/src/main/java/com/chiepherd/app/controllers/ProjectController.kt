package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.chiepherd.models.Project
import com.jfoenix.controls.JFXTextArea
import com.jfoenix.controls.JFXTextField
import com.jfoenix.controls.JFXToggleButton
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import org.json.JSONObject
import java.net.URL
import java.util.*

class ProjectController(val project_id : String) : ApplicationController() {
    @FXML lateinit var vboxProject : VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("Je suis le projet #$project_id")
        val json = "{ \"uuid\": \"${project_id}\" }"
        val res = RabbitMQ.instance.sendMessage("chiepherd.project.show", json)

        val jsonProject = JSONObject(res)
        loadProject(Project(jsonProject))
    }

    fun loadProject(project: Project) {
        (vboxProject.children[1] as JFXTextField).text = project.name
        (vboxProject.children[2] as JFXTextField).text = project.label
        (vboxProject.children[3] as JFXToggleButton).isSelected = project.visibility
        (vboxProject.children[4] as JFXTextArea).text = project.description
    }
}