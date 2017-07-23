package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel
import org.json.JSONObject

class Task(json: JSONObject) : ApplicationModel() {
    var title                    : String? = json["title"] as String?
    var description              : String? = json["description"] as String?
    var uuid                     : String? = json["uuid"] as String?
    var points                   : Integer? = json["points"] as Integer?
    var type                     : String? = json["type"] as String?
    var priority                 : Integer? = json["points"] as Integer?
    var ancestor                 : String? = json["ancestor"] as String?
    lateinit var children                 : Array<String>
}