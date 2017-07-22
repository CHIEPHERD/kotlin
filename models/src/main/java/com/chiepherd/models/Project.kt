package com.chiepherd.models

import com.chiepherd.core.models.ApplicationModel
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.KProperty

class Project(jsonObject: JSONObject) : ApplicationModel() {
    var uuid        : String? = jsonObject["uuid"] as String?
    var name        : String? = jsonObject["name"] as String?
    var label       : String? = jsonObject["label"] as String?
    var description : String? = jsonObject["description"] as String?
    var visibility  : Boolean by lazy {
        try {
            (jsonObject["visibility"] as Boolean?) ?: true
        } catch (e : JSONException) {
            true
        }
    }

    override fun toString(): String {
        return "Project(uuid=$uuid, name=$name, label=$label, description=$description)"
    }
}

private operator fun  Any.setValue(project: Project, property: KProperty<*>, b: Boolean) {}
