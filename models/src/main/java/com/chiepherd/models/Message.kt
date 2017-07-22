package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class Message : ApplicationModel() {
    lateinit var email                 : String
    lateinit var message               : String
    lateinit var channelUuid           : String
    lateinit var uuid                  : String
    lateinit var createdAt             : String
}