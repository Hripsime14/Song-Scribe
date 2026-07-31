package com.song.scribe

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.song.demos.presentation.demos.DemosScreenRoot
import com.song.demos.presentation.details.DemoDetailScreenRoot

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
                onNavigateToDetail = {
                    navController.navigate("detail")
                }
            )
        }
        composable(route = "detail") {
            DemoDetailScreenRoot(
                onSaveChanges = {
                    navController.navigateUp()
                }
            )
        }
    }
}