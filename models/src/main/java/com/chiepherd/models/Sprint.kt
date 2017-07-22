package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class Sprint : ApplicationModel() {
    lateinit var uuid                  : String
    lateinit var begin                 : String
    lateinit var end                   : String
    lateinit var projectUuid           : String
    var active : Boolean = true
}