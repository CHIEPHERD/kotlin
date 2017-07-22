package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class ChannelAssignment : ApplicationModel() {
    lateinit var uuid                   : String
    lateinit var channelUuid            : String
    lateinit var email                  : String
}