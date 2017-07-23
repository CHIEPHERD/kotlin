package com.chiepherd.models

import com.chiepherd.core.extensions.json.withDefault
import com.chiepherd.core.models.ApplicationModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.reflect.KProperty

class Task(json: JSONObject) : ApplicationModel() {
    var title                    : String? = json["title"] as String?
    var description              : String? = json["description"] as String?
    var uuid                     : String? = json["uuid"] as String?
    var type                     : String? = json["type"] as String?
    val points : Int = json.withDefault("points", 0)
    val priority : Int = json.withDefault("priority", 0)
    val ancestor : String? = json.withDefault<String?>("ancestor", null)
    val children : List<String> = json.withDefault("children", mutableListOf<String>())

    override fun toString(): String {
        return "Task(title=$title, description=$description, uuid=$uuid, type=$type, points=$points, priority=$priority, ancestor=$ancestor, children=$children)"
    }
}

private operator fun  Any.setValue(task: Task, property: KProperty<*>, i: Int) {}
