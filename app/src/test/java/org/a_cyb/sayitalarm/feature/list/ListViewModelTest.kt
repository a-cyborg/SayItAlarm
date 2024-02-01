package org.a_cyb.sayitalarm.feature.list

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.feature.list.ListUiState
import org.a_cyb.sayitalarm.feature.list.ListViewModel
import org.a_cyb.sayitalarm.testing.TestAlarmRepository
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {

    private val alarmRepository = TestAlarmRepository()
    private val testDispatcher = UnconfinedTestDispatcher()


    private lateinit var viewModel: ListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListViewModel(alarmRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun stateIsStartingByLoading() =
        runTest {
            assertEquals(ListUiState.Loading, viewModel.listUiState.value)
        }

    @Test
    fun stateIsSuccessAfterAlarmsLoaded() =
        runTest {
            val collectedAlarmsJob = launch(testDispatcher) {
                viewModel.listUiState.collect()
            }

            alarmRepository.sendAlarms()

            assertEquals(
                ListUiState.Success(emptyList()),
                viewModel.listUiState.value
            )

            collectedAlarmsJob.cancel()
        }
}