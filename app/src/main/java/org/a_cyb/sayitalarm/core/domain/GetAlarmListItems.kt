package org.a_cyb.sayitalarm.core.domain

//class GetAlarmListItems @Inject constructor(
//    private val alarmRepository: AlarmRepository,
//    private val userDataRepository: UserDataRepository,
//) {
//    operator fun invoke(): Flow<List<AlarmListItem>> =
//        alarmRepository.getAlarmSortedByTime()
//            .combine(userDataRepository.userData) { alarms, userData ->
//                alarms.map { alarm ->
//                    alarm.toAlarmListItem(userData.is24HourFormat)
//                }
//            }
//}