package com.chiepherd.core.extensions.javafx.event

import javafx.event.ActionEvent
import javafx.scene.Node
import javafx.stage.Stage

val ActionEvent.stage : Stage
    get() = (source as Node).scene.window as Stage
