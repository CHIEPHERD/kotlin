package com.chiepherd.app.controllers

import com.chiepherd.core.controllers.ApplicationController
import java.net.URL
import java.util.*

class ProjectController(val project_id : String) : ApplicationController() {

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("Je suis le projet #$project_id")
    }
}