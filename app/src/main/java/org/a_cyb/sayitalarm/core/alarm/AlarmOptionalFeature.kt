package org.a_cyb.sayitalarm.core.alarm

/**
 * Optional features for alarm.
 */
interface AlarmOptionalFeature

data class DreamQuestion(val question: String): AlarmOptionalFeature

data object NoOptionalFeature: AlarmOptionalFeature

