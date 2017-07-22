package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class ProjectAssignment : ApplicationModel() {
    lateinit var email                   : String
    lateinit var taskUuid                : String
    lateinit var uuid                    : String
}