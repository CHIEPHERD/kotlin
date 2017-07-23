package com.chiepherd.app.controllers.kanban

import com.chiepherd.app.controllers.TaskController
import com.chiepherd.core.annotations.ChiepherdPlugin
import com.chiepherd.models.Task
import com.chiepherd.models.State
import com.chiepherd.models.Project
import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.RabbitMQ
import com.jfoenix.controls.JFXComboBox
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*

@ChiepherdPlugin(fxml = "kanban.fxml", name = "Kanban")
class KanbanController : ApplicationController() {
    lateinit var tasks : Task
    lateinit var states : State
    lateinit var project : Project

    @FXML lateinit var kanban : BorderPane
    @FXML lateinit var hStates : HBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        var json = """{ "email": "email@test.fr" }"""
        var res = RabbitMQ.instance.sendMessage("chiepherd.user.projects", json)
        val projects = JSONArray(res)
        loadProjects(projects)

        json = """{ "projectUuid": "${project.uuid}" }"""
        res = RabbitMQ.instance.sendMessage("kanban.project.states", json)
        val states = JSONArray(res)
        loadStates(states)
    }

    fun loadProjects(projects : JSONArray) {
        val items = mutableListOf<String>()
        projects.forEach {
            items.add(((it as JSONObject)["project"] as JSONObject)["name"] as String)
        }
        ((kanban.children[0] as GridPane).children[1] as JFXComboBox<*>).items.addAll(items as Collection<Nothing>)
        ((kanban.children[0] as GridPane).children[1] as JFXComboBox<*>).value = items[0]
        project = Project((projects[0] as JSONObject)["project"] as JSONObject)
    }

    fun loadStates(states : JSONArray) {
        states.forEach {
            val loader = FXMLLoader()
            loader.location = this.javaClass.classLoader.getResource("chiepherd/views/components/kanban/kanban_state.fxml")
            val json = """{ "stateUuid": "${(it as JSONObject)["uuid"]}" }"""
            val res = RabbitMQ.instance.sendMessage("kanban.state.tasks", json)
            val tasks = JSONArray(res)

            val StateComponent = loader.load<Pane>()
            StateComponent.id = it["uuid"] as String?
            (StateComponent.children[0] as Label).text = it["name"] as String

            StateComponent.children.addAll(loadTasks(tasks))
            hStates.children.add(StateComponent)
        }
    }

    fun loadTasks(tasks : JSONArray) : List<Node>  {
        val column = mutableListOf<Node>()
        tasks.forEach {
            val loader = FXMLLoader()
            loader.location = this.javaClass.classLoader.getResource("chiepherd/views/components/kanban/kanban_task.fxml")
            val taskComponent = loader.load<Pane>()
            taskComponent.id = (it as JSONObject)["uuid"] as String?
            (taskComponent.children[0] as Label).text = it["title"] as String
            (taskComponent.children[1] as Label).text = ""
            column.add(taskComponent)
        }
        return column
    }

    @FXML fun onChangeProject(actionEvent: ActionEvent?) {
        if (actionEvent == null) { return }
        val value = ((kanban.children[0] as GridPane).children[1] as JFXComboBox<*>).value

        var json = """{ "email": "email@test.fr" }"""
        var res = RabbitMQ.instance.sendMessage("chiepherd.user.projects", json)
        val projects = JSONArray(res)

        projects.forEach {
            if (((it as JSONObject)["project"] as JSONObject)["name"] == value) {
                project = Project(it["project"] as JSONObject)
            }
        }

        json = """{ "projectUuid": "${project.uuid}" }"""
        res = RabbitMQ.instance.sendMessage("kanban.project.states", json)
        val states = JSONArray(res)
        hStates.children.clear()
        loadStates(states)
    }
}
