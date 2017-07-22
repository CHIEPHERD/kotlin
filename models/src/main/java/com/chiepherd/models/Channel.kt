package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class Channel : ApplicationModel() {
    lateinit var uuid                     : String
    lateinit var name                     : String
}