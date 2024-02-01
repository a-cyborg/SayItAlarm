package org.a_cyb.sayitalarm.testing

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.a_cyb.sayitalarm.core.data.repository.AlarmRepository
import org.a_cyb.sayitalarm.core.model.Alarm

class TestAlarmRepository : AlarmRepository {
    private val cachedAlarms: MutableList<Alarm> = mutableListOf()

    private val alarmsFlow: MutableSharedFlow<List<Alarm>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getAlarms(): Flow<List<Alarm>> = flowOf(cachedAlarms)

    override fun getAlarmSortedByTime(): Flow<List<Alarm>> =
        flowOf(
            cachedAlarms.sortedBy { alarm -> alarm.combinedMinutes.value }
        )

    override fun getAlarmById(id: Int): Flow<Alarm?> =
        flowOf(
            cachedAlarms.find {
                it.id == id.toString()
            }
        )

    override suspend fun insertAlarm(alarm: Alarm): String {
        cachedAlarms.add(alarm)
        return alarm.id
    }

    override suspend fun updateAlarm(id: String, enabled: Boolean): Int {
        val alarm = getAlarmById(id.toInt()).first()

        return if (alarm == null) {
            0
        } else {
            cachedAlarms.remove(alarm)
            cachedAlarms.add(alarm.copy(enabled = enabled))

            1
        }
    }

    override suspend fun deleteAlarm(alarmId: Int): Int =
        if (cachedAlarms.remove(getAlarmById(alarmId).first())) 1 else 0

    /**
     * A test-only function to controlling the list of alarms from tests.
     */
    fun sendAlarms(alarms: List<Alarm> = emptyList()) = cachedAlarms.addAll(alarms)
}