/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.alarm_service.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.a_cyb.sayitalarm.design_system.screen.AlarmScreen
import org.a_cyb.sayitalarm.design_system.screen.SayItScreen
import org.a_cyb.sayitalarm.presentation.viewmodel.AlarmViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SayItViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SiaAlarmServiceNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.ALARM_ROUTE,
    ) {
        composable(Route.ALARM_ROUTE) {
            BackHandler(true) {  /* No operation. */ }
            AlarmScreen(
                viewModel = koinViewModel<AlarmViewModel>(),
                navigateToSayIt = navController::navigateToSayIt
            )
        }
        composable(Route.SAY_IT_ROUTE) {
            BackHandler(true) {  /* No operation. */ }
            SayItScreen(koinViewModel<SayItViewModel>())
        }
    }
}

private object Route {
    const val ALARM_ROUTE = "alarm_route"
    const val SAY_IT_ROUTE = "sayIt_route"
}

private fun NavHostController.navigateToSayIt() = navigate(Route.SAY_IT_ROUTE)
