package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class TaskAssignment : ApplicationModel() {
    lateinit var email                   : String
    lateinit var taskUuid                : String
}