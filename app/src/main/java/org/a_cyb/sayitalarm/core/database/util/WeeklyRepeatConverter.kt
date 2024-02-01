package org.a_cyb.sayitalarm.core.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.a_cyb.sayitalarm.core.model.WeeklyRepeat

class WeeklyRepeatConverter {
    @TypeConverter
    fun repeatToJson(value: WeeklyRepeat?): String? =
        Gson().toJson(value)

    @TypeConverter
    fun jsonToRepeat(value: String?): WeeklyRepeat? =
        Gson().fromJson(value, WeeklyRepeat::class.java)
}