package org.a_cyb.sayitalarm.core.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson

class StringListConverter {
    @TypeConverter
    fun listToJson(value: List<String>?): String? =
        Gson().toJson(value)

    @TypeConverter
    fun <T> jsonToList(value: String): List<String>? =
        Gson().fromJson(value, Array<String>::class.java)?.toList()
}