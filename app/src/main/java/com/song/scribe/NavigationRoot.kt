package com.song.scribe

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.song.demos.presentation.addnewdemo.AddNewDemoScreenRoot
import com.song.demos.presentation.demodetail.DemoDetailsScreenRoot
import com.song.demos.presentation.demos.DemosScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "demos_graph"
    ) {
        demosGraph(navController = navController)
    }
}

private fun NavGraphBuilder.demosGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = "demos",
        route = "demos_graph"
    ) {
        composable(route = "demos") {
            DemosScreenRoot(
                onNavigateToAddNewDemo = {
                    navController.navigate("add_new_demo")
                },
                onNavigateToDemoDetails = { demoId ->
                    navController.navigate("demo_details/$demoId")
                }
            )
        }
        composable(route = "add_new_demo") {
            AddNewDemoScreenRoot(
                onSaveChanges = {
                    navController.navigateUp()
                },
                onDemoCreated = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = "demo_details/{demoId}",
            arguments = listOf(navArgument("demoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val demoId = backStackEntry.arguments?.getString("demoId") ?: return@composable
            DemoDetailsScreenRoot(
                demoId = demoId,
                onSaveChanges = {
                    navController.navigateUp()
                },
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}