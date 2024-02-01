package org.a_cyb.sayitalarm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import org.a_cyb.sayitalarm.feature.add.AddDialog
import org.a_cyb.sayitalarm.feature.edit.EditDialog
import org.a_cyb.sayitalarm.feature.list.ListScreen
import org.a_cyb.sayitalarm.feature.settings.SettingsDialog

const val listRoute = "list_route"
const val addRoute = "add_route"
const val settingsRoute = "settings_route"
const val editRoute = "edit_route"

@Composable
fun SiaNavHost(
    navController: NavHostController,
    startDestination: String = listRoute,
    onEditMode: Boolean,
    offEditMode: () -> Unit,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(listRoute) {
            ListScreen(
                onEditMode = onEditMode,
                offEditMode = offEditMode,
                navigateToEdit = navController::navigateToEdit
            )
        }
        composable(addRoute) {
//            AddDialog(onDismiss = { navController.navigateToList() })
            AddDialog(onDismiss = navController::navigateToList)
        }
        composable(route = settingsRoute) {
            SettingsDialog(onDismiss = navController::navigateToList)
        }
        composable(
            route = "${editRoute}/{alarmId}",
            arguments = listOf(navArgument("alarmId") { type = NavType.StringType })
        ) {
            EditDialog(onDismiss = navController::navigateToList)
        }
    }
}

fun NavHostController.navigateToList() = this.navigate(listRoute)
fun NavHostController.navigateToAdd() = this.navigate(addRoute)
fun NavHostController.navigateToSettings() = this.navigate(settingsRoute)
fun NavHostController.navigateToEdit(id: Int) = this.navigate("${editRoute}/$id")
