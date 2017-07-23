package com.chiepherd.kanban

import com.chiepherd.core.annotations.ChiepherdPlugin
import com.chiepherd.models.Task
import com.chiepherd.models.State
import com.chiepherd.models.Project
import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import org.json.JSONObject
import java.net.URL
import java.util.*

@ChiepherdPlugin(fxml = "kanban.fxml", name = "Kanban")
class KanbanController : ApplicationController() {
    lateinit var tasks : Task
    lateinit var states : State
    lateinit var projects : Project

    @FXML lateinit var kanban : BorderPane

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val json = "{ \"email\": \"email@test.fr\" }"
        val res = RabbitMQ.instance.sendMessage("chiepherd.user.projects", json)
        val projectJson = JSONObject(res)
        projects = Project(projectJson)
        loadProjects(projects)
    }

    fun loadProjects(project: Project) {
        kanban.children
    }
}
