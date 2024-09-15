package org.a_cyb.sayitalarm.util.formatter.time

import org.a_cyb.sayitalarm.entity.Hour
import org.a_cyb.sayitalarm.entity.Minute

interface TimeFormatterContract {
    fun format(hour: Hour, minutes: Minute): String
}
