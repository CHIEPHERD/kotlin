package com.chiepherd.app

import com.chiepherd.app.plugins.PluginLoader
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File

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
    }

    override fun stop() {
        // RabbitMQ.instance.stop()
    }

    /**
     * Initializes the root layout.
     */
    fun initRootLayout() {
        // Load root layout from fxml file.
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/layouts/Application.fxml")
        rootLayout = loader.load<Any>() as VBox
        contentLayout = rootLayout.lookup("#Content") as AnchorPane

        // Show the scene containing the root layout.
        val scene = Scene(rootLayout)
        primaryStage.scene = scene
        primaryStage.show()
    }

    /**
     * Shows the Login page
     */
    fun showLoginPage() {
        val loader = FXMLLoader()
        loader.location = this.javaClass.classLoader.getResource("chiepherd/views/Login.fxml")
        val loginView = loader.load<BorderPane>()

        contentLayout.children.add(loginView)
    }
}

fun main(args: Array<String>) {
    // RabbitMQ.instance
    Application.launch(Main::class.java, *args)
}
