package com.chiepherd.models

import com.chiepherd.core.extensions.json.withDefault
import com.chiepherd.core.models.ApplicationModel
import org.json.JSONException
import org.json.JSONObject
import kotlin.reflect.KProperty

class Project(json: JSONObject) : ApplicationModel() {
    var uuid        : String? = json["uuid"] as String?
    var name        : String? = json["name"] as String?
    var label       : String? = json["label"] as String?
    var description : String? = json["description"] as String?
    val visibility : Boolean = json.withDefault("visibility", true) as Boolean

    override fun toString(): String {
        return "Project(uuid=$uuid, name=$name, label=$label, description=$description)"
    }
}

private operator fun  Any.setValue(project: Project, property: KProperty<*>, b: Boolean) {}
