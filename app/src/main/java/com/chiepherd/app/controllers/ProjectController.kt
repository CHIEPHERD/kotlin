package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.chiepherd.models.Project
import com.jfoenix.controls.JFXTextArea
import com.jfoenix.controls.JFXTextField
import com.jfoenix.controls.JFXToggleButton
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView
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

class ProjectController(val project_id : String, val rank : String) : ApplicationController() {
    lateinit var project : Project
    @FXML lateinit var vboxProject : VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("Je suis le projet #$project_id")
        val json = "{ \"uuid\": \"${project_id}\" }"
        val res = RabbitMQ.instance.sendMessage("chiepherd.project.show", json)
        println(res)
        val jsonProject = JSONObject(res)
        project = Project(jsonProject)
        loadProject()
    }

    fun loadProject() {
        if (rank != "Lead") {
            (vboxProject.children[0] as GridPane).children.remove((vboxProject.children[0] as GridPane).children[1])
        } else {
            (vboxProject.children[0] as GridPane).children[1].onMouseClicked = EventHandler {
                val root = (vboxProject.parent.parent as AnchorPane)
                val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/project/project_edit.fxml"))
                fxmlLoader.setController(EditProjectController(project))
                root.children.clear()
                root.children.add(fxmlLoader.load())
            }
        }
        println("stop")
        (vboxProject.children[2] as JFXTextField).text = project.name
        (vboxProject.children[3] as JFXTextField).text = project.label
        (vboxProject.children[4] as JFXToggleButton).isSelected = project.visibility
        (vboxProject.children[5] as JFXTextArea).text = project.description
    }

    @FXML fun onTasks(actionEvent: ActionEvent?) {
        val root = (vboxProject.parent.parent as AnchorPane)
        val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/task/tasks.fxml"))
        fxmlLoader.setController(TasksController(project.uuid!!))
        root.children.clear()
        root.children.add(fxmlLoader.load())
    }

    @FXML fun onCancel(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/project/projects.fxml"))
    }
}