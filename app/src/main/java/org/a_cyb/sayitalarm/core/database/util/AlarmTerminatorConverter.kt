package org.a_cyb.sayitalarm.core.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.a_cyb.sayitalarm.core.alarm.AlarmTerminator
import org.a_cyb.sayitalarm.core.alarm.PushButtonTerminator
import org.a_cyb.sayitalarm.core.alarm.VoiceRecognitionTerminator
import java.lang.reflect.Type

class AlarmTerminatorConverter {
    private val gson by lazy { GsonBuilder()
        .registerTypeAdapter(AlarmTerminator::class.java, TerminatorAdapter)
        .create()
    }

    @TypeConverter
    fun terminatorToJson(value: AlarmTerminator?): String? =
        gson.toJson(value, AlarmTerminator::class.java)

    @TypeConverter
    fun jsonToTerminator(value: String?): AlarmTerminator? =
        gson.fromJson(value, AlarmTerminator::class.java)
}

object TerminatorAdapter : JsonSerializer<AlarmTerminator?>, JsonDeserializer<AlarmTerminator?> {

    private const val CLASS_MEMBER_NAME = "class"

    override fun serialize(
        src: AlarmTerminator?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement =
        Gson().toJsonTree(src).apply {
            this.asJsonObject
                .addProperty(CLASS_MEMBER_NAME, "${src?.javaClass?.simpleName}")
        }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): AlarmTerminator? {
        if (json == null || context == null) return null

        val jsonObject = kotlin.runCatching { json.asJsonObject }
            .getOrNull() ?: return null

        return when (jsonObject[CLASS_MEMBER_NAME].asString) {
            VoiceRecognitionTerminator::class.java.simpleName ->
                context.deserialize(jsonObject, VoiceRecognitionTerminator::class.java)
            PushButtonTerminator::class.java.simpleName ->
                context.deserialize(jsonObject, PushButtonTerminator::class.java)
            else -> null
        }
    }
}