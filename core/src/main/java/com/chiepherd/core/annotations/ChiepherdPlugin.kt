package com.chiepherd.core.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ChiepherdPlugin(val fxml : String, val name : String, val internal : Boolean = false)
