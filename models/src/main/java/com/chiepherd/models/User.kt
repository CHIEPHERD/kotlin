package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel

class User : ApplicationModel() {
    lateinit var email                  : String
    lateinit var firstname              : String
    lateinit var lastname               : String
    lateinit var description            : String
    lateinit var password               : String
    lateinit var password_confirmation  : String
    var isAdmin  : Boolean = false
    var isActive : Boolean = true
}
