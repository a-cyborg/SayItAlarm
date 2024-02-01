package org.a_cyb.sayitalarm.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import org.a_cyb.sayitalarm.UserPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {

    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
            UserPreferences.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Can not read proto", e)
        }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

// TODO : Why defaultValue does not override by under the code?
//    private val defaultUserData
//        get() = UserData()
//
//    private val default = UserPreferences.getDefaultInstance().toBuilder()
//        .setTimeOut(defaultUserData.timeOutInMin)
//        .setSnoozeLength(defaultUserData.snoozeLength)
//        .setRingtoneCrescendoDuration(defaultUserData.ringtoneCrescendoDuration)
//        .build()
//
//    override val defaultValue: UserPreferences = default
