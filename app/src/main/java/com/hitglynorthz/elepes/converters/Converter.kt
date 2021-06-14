package com.hitglynorthz.elepes.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hitglynorthz.elepes.models.Type

class Converter {

    @TypeConverter
    fun fromType(type: Type): String {
        return type.name
    }

    @TypeConverter
    fun toType(type: String): Type {
        return Type.valueOf(type)
    }

    /*@TypeConverter
    fun fromList(value : List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)*/

    @TypeConverter
    fun fromString(value: String?): ArrayList<String> {
        val listType = object: TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String?>): String {
        return Gson().toJson(list)
    }

    /*@TypeConverter
    fun fromString(value: String): Map<String, String> {
        val mapType = object : TypeToken<Map<String, String>>(){}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<String, String>): String {
        val gson = Gson()
        return gson.toJson(map)
    }*/

}