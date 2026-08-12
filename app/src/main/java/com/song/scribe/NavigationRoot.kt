package com.song.scribe

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.song.demos.presentation.addnewdemo.AddNewDemoScreenRoot
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
    }
}