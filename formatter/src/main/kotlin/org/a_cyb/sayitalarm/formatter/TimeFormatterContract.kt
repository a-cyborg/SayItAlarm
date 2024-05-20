package org.a_cyb.sayitalarm.formatter

import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute

interface TimeFormatterContract {
    fun formatTime(hour: Hour, minutes: Minute): String
    fun formatDuration(minutes: Int): String
}