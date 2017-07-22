package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class Project : ApplicationModel() {
    lateinit var uuid                   : String
    lateinit var name                   : String
    lateinit var label                  : String
    lateinit var description            : String
    var visibility  : Boolean = true
}
