package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.models.Task
import javafx.fxml.FXML
import javafx.scene.layout.VBox
import java.net.URL
import java.util.*

class EditTaskController(val task : Task) : ApplicationController() {
    @FXML lateinit var vboxTask: VBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {

    }
}