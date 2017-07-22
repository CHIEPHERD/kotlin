package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class State : ApplicationModel() {
    lateinit var uuid                     : String
    lateinit var name                     : String
    lateinit var level                    : Integer
    lateinit var projectUuid              : String
}