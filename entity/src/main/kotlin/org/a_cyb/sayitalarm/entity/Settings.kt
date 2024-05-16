package org.a_cyb.sayitalarm.entity

@JvmInline
value class TimeOut(val timeOut: Int)

@JvmInline
value class Snooze(val snooze: Int)

enum class Theme {
    LIGHT,
    DARK,
}

data class Settings(
    val timeOut: TimeOut,
    val snooze: Snooze,
    val theme: Theme,
)
