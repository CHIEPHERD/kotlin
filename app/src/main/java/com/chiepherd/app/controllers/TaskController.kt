package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.chiepherd.models.Project
import com.chiepherd.models.Task
import com.jfoenix.controls.JFXTextArea
import com.jfoenix.controls.JFXTextField
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.VBox
import org.json.JSONObject
import java.net.URL
import java.util.*

class TaskController(val task_id : String) : ApplicationController() {
    @FXML lateinit var vboxTask: VBox
    lateinit var task : Task

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val json = "{ \"uuid\": \"${task_id}\" }"
        val res = RabbitMQ.instance.sendMessage("chiepherd.task.show", json)
        val jsonTask = JSONObject(res)
        task = Task(jsonTask)
        loadTask(task)
    }

    fun loadTask(task : Task) {
        (vboxTask.children[1] as JFXTextField).text = task.title
        (vboxTask.children[2] as JFXTextArea).text = task.description

        (vboxTask.children[0] as GridPane).children[1].onMouseClicked = EventHandler {
            val root = (vboxTask.parent.parent as AnchorPane)
            val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/task_edit.fxml"))
            fxmlLoader.setController(EditTaskController(task))
            root.children.clear()
            root.children.add(fxmlLoader.load())
        }
    }

    @FXML fun onEdit(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val root = (vboxTask.parent.parent as AnchorPane)
        val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/task_edit.fxml"))
        fxmlLoader.setController(EditTaskController(task))
        root.children.clear()
        root.children.add(fxmlLoader.load())
    }
}