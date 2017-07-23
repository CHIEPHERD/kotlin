package com.chiepherd.kanban

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
import javafx.scene.input.ClipboardContent
import javafx.scene.input.DataFormat
import javafx.scene.input.TransferMode
import javafx.scene.layout.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import java.util.*

@ChiepherdPlugin(fxml = "home.fxml", name = "Kanban")
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
        println(res)
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
        states.forEach { stateData ->
            val loader = FXMLLoader()
            loader.location = this.javaClass.classLoader.getResource("state.fxml")
            val json = """{ "stateUuid": "${(stateData as JSONObject)["uuid"]}" }"""
            val res = RabbitMQ.instance.sendMessage("kanban.state.tasks", json)
            val tasks = JSONArray(res)

            val stateComponent = loader.load<Pane>()
            stateComponent.id = stateData["uuid"] as String?
            (stateComponent.children[0] as Label).text = stateData["name"] as String

            stateComponent.children.addAll(loadTasks(tasks))

            stateComponent.setOnDragOver {
                println("Drag over")
                val dragBoard = it.dragboard
                if(dragBoard.hasContent(DataFormat.PLAIN_TEXT)) {
                    it.acceptTransferModes(TransferMode.MOVE)
                }
                it.consume()
            }

            stateComponent.setOnDragDropped {
                println("Dropped !")
                val dragBoard = it.dragboard
                val text = dragBoard.getContent(DataFormat.PLAIN_TEXT)
                println("Text \n$text\n")
                val jsonTask = JSONObject(text as String)

                stateComponent.children.add(taskFromJson(jsonTask))

                val jsonResponse = """{"taskUuid": "${jsonTask["uuid"]}", "stateUuid": "${stateData["uuid"]}",
                    "priority": ${stateComponent.children.size}}"""
                println(jsonResponse)
                RabbitMQ.instance.sendMessage("kanban.task.move", jsonResponse)

                it.isDropCompleted = true
                it.consume()
            }

            hStates.children.add(stateComponent)
        }
    }

    fun loadTasks(tasks : JSONArray) : List<Node>  {
        val column = mutableListOf<Node>()
        tasks.forEach {
            val taskComponent = taskFromJson(it as JSONObject)
            column.add(taskComponent)
        }
        return column
    }

    private fun taskFromJson(taskData: JSONObject): GridPane {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("task.fxml")
        val taskComponent = loader.load<GridPane>()
        println("taskFromJSON $taskData")
        taskComponent.id = taskData["uuid"] as String?
        (taskComponent.children[0] as Label).text = taskData["title"] as String
        (taskComponent.children[1] as Label).text = ""

        taskComponent.setOnDragDetected {
            val dragBoard = taskComponent.startDragAndDrop(TransferMode.MOVE)
            val content = ClipboardContent()

            content.putString(taskData.toString())

            dragBoard.setContent(content)
            it.consume()
        }

        taskComponent.setOnDragDone {
            (taskComponent.parent as Pane).children.remove(taskComponent)
        }

        taskComponent.children[3].onMouseClicked = EventHandler {
            val root = (kanban.parent as AnchorPane)
            val fxmlLoader = FXMLLoader(javaClass.classLoader.getResource("edit.fxml"))

            fxmlLoader.setController(EditTaskController(Task(taskData)))

            root.children.clear()
            root.children.add(fxmlLoader.load())
        }

        return taskComponent
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
