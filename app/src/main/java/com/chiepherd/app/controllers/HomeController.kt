package com.chiepherd.app.controllers

import javafx.fxml.FXML
import java.net.URL
import java.util.*
import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import org.json.JSONArray
import org.json.JSONObject
import com.chiepherd.models.Project
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.control.Label
import javafx.scene.layout.*

class HomeController : ApplicationController() {
    @FXML lateinit var yield : AnchorPane
    @FXML lateinit var projects : VBox

    @FXML override fun initialize(location: URL?, resources: ResourceBundle?) {

        val json = "{ \"email\": \"email@test.fr\" }"
        val res = RabbitMQ.instance.sendMessage("chiepherd.user.projects", json)

        val jsonProjects = JSONArray(res)
        val projects = mutableListOf<Project>()
        jsonProjects.forEach {
            val project = Project((it as JSONObject)["project"] as JSONObject)
            loadProject(project, it["rank"] as String)
        }
    }

    fun loadProject(project : Project, rank : String) {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/components/project.fxml")
        val projectComponent = loader.load<Pane>()
        projectComponent.id = project.uuid

        projectComponent.onMouseClicked = EventHandler {
            println("Hello from component $projectComponent")
            val root = (projects.parent.parent as AnchorPane)
            val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("chiepherd/views/project/project_show.fxml"))
            fxmlLoader.setController(ProjectController(projectComponent.id, rank))
            root.children.clear()
            root.children.add(fxmlLoader.load())
        }
        (projectComponent.children[0] as Label).text = "${project.name} (${project.label})"
        (projectComponent.children[1] as Label).text = project.description
        projects.children.add(projectComponent)
    }

    @FXML fun onNewProject(actionEvent : ActionEvent?) {
        if(actionEvent == null) { return }
        switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/project/project_new.fxml"))
    }
}
