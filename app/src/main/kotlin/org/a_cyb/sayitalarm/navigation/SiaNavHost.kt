/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.a_cyb.sayitalarm.navigation.NavRoute.ADD_ROUTE
import org.a_cyb.sayitalarm.navigation.NavRoute.EDIT_ARGUMENT_NAME
import org.a_cyb.sayitalarm.navigation.NavRoute.EDIT_ROUTE
import org.a_cyb.sayitalarm.navigation.NavRoute.EDIT_ROUTE_WITH_ARGUMENT
import org.a_cyb.sayitalarm.navigation.NavRoute.LIST_ROUTE
import org.a_cyb.sayitalarm.navigation.NavRoute.SETTINGS_ROUTE
import org.a_cyb.sayitalarm.presentation.viewmodel.AddViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.EditViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.ListViewModel
import org.a_cyb.sayitalarm.presentation.viewmodel.SettingsViewModel
import org.a_cyb.sayitalarm.screen.AddScreen
import org.a_cyb.sayitalarm.screen.EditScreen
import org.a_cyb.sayitalarm.screen.ListScreen
import org.a_cyb.sayitalarm.screen.SettingsScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SiaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = LIST_ROUTE,
    ) {
        composable(LIST_ROUTE) {
            ListScreen(
                viewModel = koinViewModel<ListViewModel>(),
                navigateToSettings = navController::navigateToSettings,
                navigateToAdd = navController::navigateToAdd,
                navigateToEdit = navController::navigateToEdit,
            )
        }

        composable(ADD_ROUTE) {
            AddScreen(
                viewModel = koinViewModel<AddViewModel>(),
                navigateToList = navController::navigateBackToList
            )
        }

        composable(
            EDIT_ROUTE_WITH_ARGUMENT,
            arguments = listOf(navArgument(EDIT_ARGUMENT_NAME) { type = NavType.LongType })
        ) {
            EditScreen(
                viewModel = koinViewModel<EditViewModel>(
                    parameters = { parametersOf(navController.getAlarmId()) }
                ),
                navigateToList = navController::navigateBackToList,
            )
        }

        composable(SETTINGS_ROUTE) {
            SettingsScreen(
                viewModel = koinViewModel<SettingsViewModel>(),
                navigateToList = navController::navigateBackToList
            )
        }
    }
}

private fun NavHostController.navigateToAdd() = navigate(ADD_ROUTE)
private fun NavHostController.navigateToEdit(id: Long) = navigate("$EDIT_ROUTE/$id")
private fun NavHostController.navigateToSettings() = navigate(SETTINGS_ROUTE)
private fun NavHostController.navigateBackToList() = popBackStack(LIST_ROUTE, false)
private fun NavHostController.getAlarmId(): Long = getBackStackEntry(EDIT_ROUTE_WITH_ARGUMENT)
    .arguments
    ?.getLong(EDIT_ARGUMENT_NAME) ?: 0

private object NavRoute {
    const val LIST_ROUTE = "list_route"
    const val ADD_ROUTE = "add_route"
    const val EDIT_ROUTE = "edit_route"
    const val SETTINGS_ROUTE = "settings_route"

    const val EDIT_ARGUMENT_NAME = "alarmId"
    const val EDIT_ROUTE_WITH_ARGUMENT = "$EDIT_ROUTE/{$EDIT_ARGUMENT_NAME}"
}
