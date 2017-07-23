@file:Suppress("UNCHECKED_CAST")

package com.chiepherd.core.extensions.json

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

fun <T> JSONObject.withDefault(key : String, default : T) : T {
    try {
        return (this[key] as T) ?: default
    } catch (e : JSONException) {
        return default
    }
}

fun <T> JSONObject.withDefault(key : String, default : List<T>) : List<T> {
    try {
        val list = mutableListOf<T>()
        (this[key] as JSONArray).forEach { list.add(it as T) }
        return list
    } catch (e : JSONException) {
        return default
    }
}
