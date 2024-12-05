/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel.di

import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.a_cyb.sayitalarm.domain.alarm_service.SttRecognizerOnDeviceHelper
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract
import org.a_cyb.sayitalarm.presentation.contracts.AddContract
import org.a_cyb.sayitalarm.presentation.contracts.AlarmContract
import org.a_cyb.sayitalarm.presentation.contracts.EditContract
import org.a_cyb.sayitalarm.presentation.contracts.ListContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SettingsContract
import org.a_cyb.sayitalarm.presentation.viewmodel.mapper.AlarmMapperContract
import org.a_cyb.sayitalarm.util.formatter.time.TimeFormatterContract
import org.a_cyb.sayitalarm.util.formatter.weekday.WeekdayFormatterContract
import org.a_cyb.sayitalarm.util.sound_effect_player.SoundEffectPlayerContract
import org.a_cyb.sayitalarm.util.time_flow.TimeFlowContract
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelModuleSpec {

    private val formatterModule = module {
        single<TimeFormatterContract> { mockk() }
        single<WeekdayFormatterContract> { mockk() }
        single<AlarmMapperContract> { mockk(relaxed = true) }
        single<SttRecognizerOnDeviceHelper> { mockk(relaxed = true) }
    }

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `It injects AddViewModel`() {
        // Given
        val addInteractorModule = module {
            single<InteractorContract.AddInteractor> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                viewModelModule,
                addInteractorModule,
                formatterModule,
            )
        }

        // When
        val viewModel = koinApp.koin.getOrNull<AddContract.AddViewModel>()

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects EditViewModel`() {
        // Given
        val interactorModule = module {
            single<InteractorContract.EditInteractor> { mockk(relaxed = true) }
        }

        val koinApp = koinApplication {
            modules(
                viewModelModule,
                interactorModule,
                formatterModule,
            )
        }

        // When
        val viewModel = koinApp.koin
            .getOrNull<EditContract.EditViewModel>(
                parameters = { parametersOf(2L) },
            )

        // Then
        assertNotNull(viewModel)
    }

    @Test
    fun `It injects ListViewModel`() {
        // Given
        val interactorModule = module {
            single<InteractorContract.ListInteractor> { mockk(relaxed = true) }
        }

        val koinApp = koinApplication {
            modules(
                viewModelModule,
                interactorModule,
                formatterModule,
            )
        }

        // When
        val listViewModel = koinApp.koin.getOrNull<ListContract.ListViewModel>()

        // Then
        assertNotNull(listViewModel)
    }

    @Test
    fun `It injects SettingsViewModel`() {
        // Given
        val externalModule = module {
            single<InteractorContract.SettingsInteractor> { mockk(relaxed = true) }
            single<org.a_cyb.sayitalarm.util.formatter.duration.DurationFormatterContract> { mockk() }
            single<org.a_cyb.sayitalarm.util.link_opener.LinkOpenerContract> { mockk() }
        }

        val koinApp = koinApplication {
            modules(
                viewModelModule,
                externalModule,
            )
        }

        // When
        val settingsViewModel = koinApp.koin.getOrNull<SettingsContract.SettingsViewModel>()

        // Then
        assertNotNull(settingsViewModel)
    }

    @Test
    fun `It injects AlarmViewModel`() {
        // Given
        val externalModule = module {
            single<InteractorContract.AlarmInteractor> { mockk(relaxed = true) }
            single<TimeFlowContract> { mockk(relaxed = true) }
        }

        val koinApp = koinApplication {
            modules(
                viewModelModule,
                formatterModule,
                externalModule,
            )
        }

        // When
        val alarmViewModel = koinApp.koin
            .getOrNull<AlarmContract.AlarmViewModel>(
                parameters = { parametersOf(0) },
            )

        // Then
        assertNotNull(alarmViewModel)
    }

    @Test
    fun `It injects SayItViewModel`() {
        // Given
        val externalModule = module {
            single<InteractorContract.SayItInteractor> { mockk(relaxed = true) }
            single<SoundEffectPlayerContract> { mockk(relaxed = true) }
        }

        val koinApp = koinApplication {
            modules(
                viewModelModule,
                externalModule,
            )
        }

        // When
        val viewModel = koinApp.koin.getOrNull<SayItContract.SayItViewModel>()

        // Then
        assertNotNull(viewModel)
    }
}
