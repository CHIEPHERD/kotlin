package com.chiepherd.models
import com.chiepherd.core.models.ApplicationModel

class Comment : ApplicationModel() {
    lateinit var uuid                   : String
    lateinit var message                : String
    lateinit var createdAt              : String
    lateinit var updatedAt              : String
    lateinit var taskUuid               : String
    lateinit var email                  : String
}