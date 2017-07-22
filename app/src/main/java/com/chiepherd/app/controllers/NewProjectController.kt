package com.chiepherd.app.controllers

import javafx.fxml.FXML
import java.net.URL
import java.util.*
import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import org.json.JSONArray
import org.json.JSONObject
import com.chiepherd.models.Project
import com.jfoenix.controls.JFXTextArea
import com.jfoenix.controls.JFXTextField
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.*

class NewProjectController : ApplicationController() {
    @FXML lateinit var yield : AnchorPane
    @FXML lateinit var formProject : VBox

    @FXML override fun initialize(location: URL?, resources: ResourceBundle?) { }


    @FXML fun onCreateProject(actionEvent : ActionEvent?) {
        if(actionEvent == null) { return }
        val json =
            """{ "name": "${(formProject.children[1] as JFXTextField).text}",
                 "label": "${(formProject.children[2] as JFXTextField).text}",
                 "description": "${(formProject.children[3] as JFXTextArea).text}",
                 "email": "email@test.fr" }"""
//        val project = Project(JSONObject(json))
        // TODO: Check validation errors

        val res = RabbitMQ.instance.sendMessage("chiepherd.project.create", json)
        println(res)
        // TODO: Check server errors
        if (JSONObject(res).has("uuid")) {
            Thread.sleep(1_000)
            switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/project/projects.fxml"))
        }
    }

    @FXML fun onCancel(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/project/projects.fxml"))
    }
}
