package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.chiepherd.models.Project
import com.chiepherd.models.Task
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*

class TasksController(val project_id : String) : ApplicationController() {
    @FXML lateinit var tasks : VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val json = "{ \"uuid\": \"${project_id}\" }"
        val res = RabbitMQ.instance.sendMessage("chiepherd.project.tasks", json)
        val jsonProjects = JSONArray(res)
        jsonProjects.forEach {
            val task = Task(it as JSONObject)
            loadTasks(task)
        }
    }

    fun loadTasks(task: Task) {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/components/task.fxml")
        val taskComponent = loader.load<Pane>()
        taskComponent.id = task.uuid

        taskComponent.onMouseClicked = EventHandler {
            println("Hello from component $taskComponent")
            val root = (tasks.parent.parent as AnchorPane)
            val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/task_show.fxml"))
            fxmlLoader.setController(TaskController(task.uuid!!))
            root.children.clear()
            root.children.add(fxmlLoader.load())
        }
        (taskComponent.children[0] as Label).text = task.title
        tasks.children.add(taskComponent)
                
        println(task)
    }

    @FXML fun onNewTask(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val root = (tasks.parent.parent as AnchorPane)
        val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/task_new.fxml"))
        fxmlLoader.setController(NewTaskController(project_id))

        root.children.clear()
        root.children.add(fxmlLoader.load())
    }
}

