package org.a_cyb.sayitalarm.ringtone_manager

interface RingtoneManagerContract {
    fun getRingtoneTitle(ringtone: String): String
    fun getDefaultRingtone(): String
}