package com.chiepherd.kanban

import com.chiepherd.core.annotations.ChiepherdPlugin
import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.models.Project
import com.chiepherd.models.State
import com.chiepherd.models.Task
import javafx.fxml.FXML
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import java.net.URL
import java.util.*

class EditTaskController(task: Task) : ApplicationController() {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
    }
}
