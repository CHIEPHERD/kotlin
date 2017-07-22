package com.chiepherd.app.controllers

import com.chiepherd.app.plugins.PluginList
import com.chiepherd.core.controllers.ApplicationController
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXTextField
import javafx.event.ActionEvent
import javafx.fxml.FXML
import java.net.URL
import java.util.*
import khttp.post

class SignUpController : ApplicationController() {
    @FXML lateinit var first_name            : JFXTextField
    @FXML lateinit var last_name             : JFXTextField
    @FXML lateinit var email                 : JFXTextField
    @FXML lateinit var password              : JFXPasswordField
    @FXML lateinit var password_confirmation : JFXPasswordField

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        PluginList.instance.plugins.forEach {
            println(it.name)
        }
        println("Sign UP Controller")
    }

    @FXML fun onSignIn(actionEvent : ActionEvent?) {
        if(actionEvent == null) { return }
        val payload = mapOf("email"                 to email.text,
                            "password"              to password.text,
                            "firstname"             to first_name.text,
                            "lastname"              to last_name.text,
                            "password_confirmation" to password_confirmation.text)
        val res = post("http://192.168.56.103:3000/auth/register", data = payload)
        if(res.statusCode != 200) {
            displayError(res)
        } else {
            switchScene(actionEvent, javaClass.classLoader.getResource("chiepherd/views/login.fxml"))
        }
    }
}
