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

class NewTaskController(val project_id : String) : ApplicationController() {
    @FXML lateinit var yield : AnchorPane
    @FXML lateinit var formTask : VBox

    @FXML override fun initialize(location: URL?, resources: ResourceBundle?) {
        println()
    }


    @FXML fun onCreateTask(actionEvent : ActionEvent?) {
        if(actionEvent == null) { return }
        val json =
                """{ "title": "${(formTask.children[1] as JFXTextField).text}",
                 "description": "${(formTask.children[2] as JFXTextArea).text}",
                 "projectUuid": "${project_id}",
                 "type": "Task",
                 "email": "email@test.fr" }"""
        // TODO: Check validation errors

        val res = RabbitMQ.instance.sendMessage("chiepherd.task.create", json)
        // TODO: Check server errors
        if (JSONObject(res).has("uuid")) {
            Thread.sleep(1_000)
            val root = (formTask.parent.parent as AnchorPane)
            val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/tasks.fxml"))
            fxmlLoader.setController(TasksController(project_id))
            root.children.clear()
            root.children.add(fxmlLoader.load())
        }
    }

    @FXML fun onCancel(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val root = (formTask.parent.parent as AnchorPane)
        val json = """{ "uuid": "${project_id}", "email": "email@test.fr" }"""
        val projects = JSONArray(RabbitMQ.instance.sendMessage("chiepherd.user.projects", json))
        var rank = ""
        projects.forEach {
            if (((it as JSONObject)["project"] as JSONObject)["uuid"] == project_id) {
                rank = it["rank"] as String
            }
        }
        if (rank.length == 0) {
            switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/project/projects.fxml"))
        } else {
            val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/project/project_show.fxml"))
            fxmlLoader.setController(ProjectController(project_id, rank))
            root.children.clear()
            root.children.add(fxmlLoader.load())
        }
    }
}
