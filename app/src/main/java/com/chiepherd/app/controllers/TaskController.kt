package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class TaskController(val task_id : String) : ApplicationController() {
    @FXML lateinit var vboxTask: VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }
}