/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.architecture.blueprints.todoapp.TodoDestinationsArgs.TASK_ID_ARG
import com.example.android.architecture.blueprints.todoapp.TodoDestinationsArgs.TITLE_ARG
import com.example.android.architecture.blueprints.todoapp.TodoDestinationsArgs.USER_MESSAGE_ARG
import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditTaskScreen
import com.example.android.architecture.blueprints.todoapp.statistics.StatisticsScreen
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailScreen
import com.example.android.architecture.blueprints.todoapp.tasks.TasksScreen
import com.example.android.architecture.blueprints.todoapp.util.AppModalDrawer
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun AppLayout() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TodoNavGraph()

        Spacer(modifier = Modifier.height(16.dp))

        AdBanner()
    }
}

@Composable
fun TodoNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = TodoDestinations.TASKS_ROUTE,
    navActions: TodoNavigationActions = remember(navController) {
        TodoNavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(
            TodoDestinations.TASKS_ROUTE,
            arguments = listOf(
                navArgument(USER_MESSAGE_ARG) { type = NavType.IntType; defaultValue = 0 }
            )
        ) { entry ->
            AppModalDrawer(drawerState, currentRoute, navActions) {
                TasksScreen(
                    userMessage = entry.arguments?.getInt(USER_MESSAGE_ARG)!!,
                    onUserMessageDisplayed = { entry.arguments?.putInt(USER_MESSAGE_ARG, 0) },
                    onAddTask = { navActions.navigateToAddEditTask(R.string.add_task, null) },
                    onTaskClick = { task -> navActions.navigateToTaskDetail(task.id) },
                    openDrawer = { coroutineScope.launch { drawerState.open() } }
                )
            }
        }
        composable(TodoDestinations.STATISTICS_ROUTE) {
            AppModalDrawer(drawerState, currentRoute, navActions) {
                StatisticsScreen(openDrawer = { coroutineScope.launch { drawerState.open() } })
            }
        }
        composable(
            TodoDestinations.ADD_EDIT_TASK_ROUTE,
            arguments = listOf(
                navArgument(TITLE_ARG) { type = NavType.IntType },
                navArgument(TASK_ID_ARG) { type = NavType.StringType; nullable = true },
            )
        ) { entry ->
            val taskId = entry.arguments?.getString(TASK_ID_ARG)
            AddEditTaskScreen(
                topBarTitle = entry.arguments?.getInt(TITLE_ARG)!!,
                onTaskUpdate = {
                    navActions.navigateToTasks(
                        if (taskId == null) ADD_EDIT_RESULT_OK else EDIT_RESULT_OK
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(TodoDestinations.TASK_DETAIL_ROUTE) {
            TaskDetailScreen(
                onEditTask = { taskId ->
                    navActions.navigateToAddEditTask(R.string.edit_task, taskId)
                },
                onBack = { navController.popBackStack() },
                onDeleteTask = { navActions.navigateToTasks(DELETE_RESULT_OK) }
            )
        }
    }
}

@Composable
fun AdBanner() {
    val context = LocalContext.current

    // Create AdView with AdSize.BANNER
    val adView = AdView(context)
    adView.setAdSize(AdSize.BANNER)
    adView.adUnitId = "ca-app-pub-1097657497287401/1320730307"

    // Load the ad
    val adRequest = AdRequest.Builder()
        .build()
    adView.loadAd(adRequest)

    // Composable to display the ad
    AndroidView(
        factory = { adView },
        modifier = Modifier.fillMaxWidth(),
        update = {
            it.resume()
        }
    )
}

// Keys for navigation
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2
const val EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 3
