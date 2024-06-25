package org.a_cyb.sayitalarm.domain.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.a_cyb.sayitalarm.domain.repository.RepositoryContract
import org.a_cyb.sayitalarm.entity.Alarm
import org.a_cyb.sayitalarm.presentation.interactor.InteractorContract

class AddInteractor(
    private val alarmRepository: RepositoryContract.AlarmRepository
) : InteractorContract.AddInteractor {

    override fun save(alarm: Alarm, scope: CoroutineScope) {
        scope.launch {
            alarmRepository
                .save(alarm, this)
        }
    }
}
