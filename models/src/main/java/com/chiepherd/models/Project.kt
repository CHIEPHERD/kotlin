package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel
import org.json.JSONObject

class Project(jsonObject: JSONObject) : ApplicationModel() {
    var uuid        : String? = jsonObject["uuid"] as String?
    var name        : String? = jsonObject["name"] as String?
    var label       : String? = jsonObject["label"] as String?
    var description : String? = jsonObject["description"] as String?

    override fun toString(): String {
        return "Project(uuid=$uuid, name=$name, label=$label, description=$description)"
    }
//    var visibility  : Boolean = (jsonObject["visibility"] as Boolean?) ?: true
}
