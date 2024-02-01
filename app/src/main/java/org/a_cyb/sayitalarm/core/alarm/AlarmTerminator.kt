package org.a_cyb.sayitalarm.core.alarm

/**
 * Methods for terminating alarm.
 */
interface AlarmTerminator {
    fun turnOff(): Boolean
}


data class VoiceRecognitionTerminator(val scripts: List<String>): AlarmTerminator {
    override fun turnOff(): Boolean {
        return true
    }
}

data object PushButtonTerminator: AlarmTerminator {
    override fun turnOff(): Boolean {
        return true
    }
}
