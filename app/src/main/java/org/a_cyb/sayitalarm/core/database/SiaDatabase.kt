package org.a_cyb.sayitalarm.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.a_cyb.sayitalarm.core.database.dao.AlarmDao
import org.a_cyb.sayitalarm.core.database.dao.AlarmInstanceDao
import org.a_cyb.sayitalarm.core.database.model.AlarmEntity
import org.a_cyb.sayitalarm.core.database.model.AlarmInstanceEntity
import org.a_cyb.sayitalarm.core.database.util.AlarmTerminatorConverter
import org.a_cyb.sayitalarm.core.database.util.OptionalFeatureConverter
import org.a_cyb.sayitalarm.core.database.util.StringListConverter
import org.a_cyb.sayitalarm.core.database.util.WeeklyRepeatConverter


@Database(
    entities = [AlarmEntity::class, AlarmInstanceEntity::class,],
    version = 1,
    exportSchema = true,
)
@TypeConverters(
    StringListConverter::class,
    WeeklyRepeatConverter::class,
    AlarmTerminatorConverter::class,
    OptionalFeatureConverter::class,
)
abstract class SiaDatabase: RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
    abstract fun alarmInstanceDao(): AlarmInstanceDao
}