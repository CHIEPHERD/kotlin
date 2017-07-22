package com.chiepherd.app.controllers

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXTextField
import javafx.event.ActionEvent
import javafx.fxml.FXML
import java.net.URL
import java.util.*
import com.chiepherd.core.controllers.ApplicationController
import com.chiepherd.core.services.MenuManager

class LoginController : ApplicationController() {
    @FXML lateinit var email    : JFXTextField
    @FXML lateinit var password : JFXPasswordField
    @FXML lateinit var sign_in  : JFXButton
    @FXML lateinit var sign_up  : JFXButton

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println("INIT View Login")
    }

    @FXML fun onSignUp(actionEvent : ActionEvent?) {
        println("Sign up")
        if(actionEvent == null) { return }
        switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/sign_up.fxml"), MenuManager.get("Registration"))
    }

    @FXML fun onSignIn(actionEvent : ActionEvent?) {
        println("Sign in")
        if(actionEvent == null) { return }
        switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/projects.fxml"))
    }
}
