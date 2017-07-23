package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.chiepherd.models.Project
import com.chiepherd.models.Task
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import org.json.JSONArray
import org.json.JSONObject
import java.awt.event.ActionEvent
import java.net.URL
import java.util.*

class TasksController(val project_id : String) : ApplicationController() {
    @FXML lateinit var tasks : VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val json = "{ \"uuid\": \"${project_id}\" }"
        val res = RabbitMQ.instance.sendMessage("chiepherd.project.tasks", json)
        println(res)
        val jsonProjects = JSONArray(res)
        val tasks = mutableListOf<Project>()
        jsonProjects.forEach {
            val task = Task(it as JSONObject)
            loadTasks(task)
        }
    }

    fun loadTasks(task: Task) {
        println(task)
    }

    @FXML fun onNewTask(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
    }
}

