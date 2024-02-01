package org.a_cyb.sayitalarm.core.database.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import org.a_cyb.sayitalarm.core.alarm.AlarmOptionalFeature
import org.a_cyb.sayitalarm.core.alarm.DreamQuestion
import org.a_cyb.sayitalarm.core.alarm.NoOptionalFeature
import java.lang.reflect.Type

class OptionalFeatureConverter {
    private val gson by lazy { GsonBuilder()
        .registerTypeAdapter(AlarmOptionalFeature::class.java, OptionalFeatureAdapter)
        .create()
    }

    @TypeConverter
    fun optionalFeatureToJson(value: AlarmOptionalFeature?): String? =
        gson.toJson(value, AlarmOptionalFeature::class.java)

    @TypeConverter
    fun jsonToOptionalFeature(value: String?): AlarmOptionalFeature? =
        gson.fromJson(value, AlarmOptionalFeature::class.java)
}

object OptionalFeatureAdapter :
    JsonSerializer<AlarmOptionalFeature>,
    JsonDeserializer<AlarmOptionalFeature>
{

    private const val CLASS_MEMBER_NAME = "class"

    override fun serialize(
        src: AlarmOptionalFeature?,
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
    ): AlarmOptionalFeature? {
        if (json == null || context == null) return null

        val jsonObject = kotlin.runCatching { json.asJsonObject }
            .getOrNull() ?: return null

        return when (jsonObject[CLASS_MEMBER_NAME].asString) {
            DreamQuestion::class.java.simpleName ->
                context.deserialize(jsonObject, DreamQuestion::class.java)
            NoOptionalFeature::class.java.simpleName ->
                context.deserialize(jsonObject, NoOptionalFeature::class.java)
            else -> null
        }
    }
}