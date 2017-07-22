package com.chiepherd.kanban

import com.chiepherd.core.annotations.ChiepherdPlugin
import com.chiepherd.core.controllers.ApplicationController
import java.net.URL
import java.util.*

@ChiepherdPlugin(fxml = "kanban.fxml", name = "Kanban")
class KanbanController : ApplicationController() {
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("Hi from Kanban view")
    }
}
