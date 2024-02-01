package org.a_cyb.sayitalarm.core.model

/**
 * Represents a 24-hour time value as a minutes elapsed from midnight.
 *
 * @property value The total minutes elapsed from midnight.
 */
data class CombinedMinutes(val value: Int) {
    /**
     * Hour in 24-hour localtime 0 - 23.
     */
    val hour: Int
        get() = value / 60

    /**
     * Minutes in localtime 0 - 59.
     */
    val minute: Int
        get() = value % 60

    init {
        require(value in 0..MAX_COMBINED_MINUTES) {
            "Invalid combined minutes value. Combined minutes must be within the range of 0 to 1439."
        }
    }

    /**
     * Construct [CombinedMinutes] with 24-Hour format hour and minute.
     */
    constructor(hour: Int, minute: Int) : this (hour * 60 + minute) {
        require(hour in 0..23) {
            "Invalid hour value. Hour must be within the range of 0 to 23."
        }
        require(minute in 0..59) {
            "Invalid minute value. Minute must be within the range of 0 to 59."
        }
    }

    companion object {
        /**
         * Represent 23:59, last minutes of the day(23 * 60 + 59).
         */
        const val MAX_COMBINED_MINUTES = 1439
    }
}