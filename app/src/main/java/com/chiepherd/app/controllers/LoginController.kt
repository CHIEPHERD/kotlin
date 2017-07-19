package com.chiepherd.app.controllers

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXTextField
import javafx.event.ActionEvent
import javafx.fxml.FXML
import java.net.URL
import java.util.*
import com.chiepherd.core.controllers.ApplicationController

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
        switchScene(actionEvent, "chiepherd/views/SignUp.fxml")
    }
}
