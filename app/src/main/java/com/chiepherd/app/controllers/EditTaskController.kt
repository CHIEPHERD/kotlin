package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.chiepherd.models.Task
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

class EditTaskController(val task : Task) : ApplicationController() {
    @FXML lateinit var vboxTask: VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        loadTask(task)
    }

    fun loadTask(task: Task) {
        (vboxTask.children[1] as JFXTextField).text = task.title
        (vboxTask.children[2] as JFXTextArea).text = task.description
    }

    @FXML fun onCancel(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val root = (vboxTask.parent.parent as AnchorPane)
        val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/task_show.fxml"))
        fxmlLoader.setController(TaskController(task.uuid!!))

        root.children.clear()
        root.children.add(fxmlLoader.load())
    }

    @FXML fun onUpdate(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val json =
                """{ "title": "${(vboxTask.children[1] as JFXTextField).text}",
                 "description": "${(vboxTask.children[2] as JFXTextArea).text}",
                 "ancestorUuid": ${null},
                 "uuid": "${task.uuid}" }"""
        val res = RabbitMQ.instance.sendMessage("chiepherd.task.update", json)

        // TODO: Check server errors
        if (JSONObject(res).has("uuid")) {
            Thread.sleep(1_000)
            val root = (vboxTask.parent.parent as AnchorPane)
            val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/task_show.fxml"))
            fxmlLoader.setController(TaskController(task.uuid!!))

            root.children.clear()
            root.children.add(fxmlLoader.load())
        }
    }
}