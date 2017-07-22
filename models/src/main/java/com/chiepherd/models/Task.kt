package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class Task : ApplicationModel() {
    lateinit var title                    : String
    lateinit var description              : String
    lateinit var uuid                     : String
    lateinit var points                   : Integer
    lateinit var type                     : String
    lateinit var priority                 : Integer
    lateinit var ancestor                 : String
    lateinit var children                 : Array<String>
}