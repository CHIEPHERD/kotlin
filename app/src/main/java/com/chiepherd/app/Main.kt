package com.chiepherd.app

import com.chiepherd.app.plugins.PluginList
import com.chiepherd.app.plugins.PluginLoader
import com.chiepherd.core.services.MenuManager
import com.jfoenix.controls.JFXButton
import com.chiepherd.core.services.RabbitMQ
import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.SplitPane
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import java.net.URL

class Main : Application() {
    lateinit var primaryStage : Stage
    lateinit var rootLayout : VBox
    lateinit var contentLayout : AnchorPane

    override fun start(primaryStage: Stage) {
        val plugins = PluginLoader(File("plugins").toURI()).load()
        this.primaryStage = primaryStage
        this.primaryStage.title = "Chiepherd"

        initRootLayout()

        showLoginPage()

        populateMenu()

        primaryStage.show()
    }

    override fun stop() {
         RabbitMQ.instance.stop()
    }

    /**
     * Initializes the root layout.
     */
    fun initRootLayout() {
        // Load root layout from fxml file.
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/layouts/application.fxml")
        rootLayout = loader.load<Any>() as VBox
        contentLayout = (rootLayout.lookup("SplitPane") as SplitPane).items[1] as AnchorPane

        // Show the scene containing the root layout.
        val scene = Scene(rootLayout)
        primaryStage.scene = scene
        // primaryStage.show()
    }

    /**
     * Shows the Login page
     */
    fun showLoginPage() {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/login.fxml")
        val loginView = loader.load<BorderPane>()

        contentLayout.children.add(loginView)
    }

    fun genButton(name : String, resource : URL, classLoader: ClassLoader? = null) : Button {
        val btn = JFXButton(name)
        btn.onAction = EventHandler {
            println("Event $name")
            val loader = FXMLLoader()
            loader.location = resource
            if(classLoader != null) {
                loader.classLoader = classLoader
            }
            val element = loader.load<BorderPane>()
            contentLayout.children.clear()
            contentLayout.children.add(element)
        }
        return btn
    }

    fun populateMenu() {
        val registration = VBox()

        registration.children.add(genButton("Login", javaClass.classLoader.getResource("chiepherd/views/Login.fxml")))
        registration.children.add(genButton("SignUp", javaClass.classLoader.getResource("chiepherd/views/SignUp.fxml")))

        MenuManager.add("Registration", registration)

        val logged = VBox()

        logged.children.add(genButton("Home", javaClass.classLoader.getResource("chiepherd/views/Conn.fxml")))
        PluginList.instance.plugins.forEach {
            logged.children.add(genButton(it.name, it.classLoader.getResource(it.fxml), it.classLoader))
        }

        MenuManager.add("Logged", logged)

        ((rootLayout.lookup("SplitPane") as SplitPane).items[0] as AnchorPane).children.add(MenuManager.get("Registration"))
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
