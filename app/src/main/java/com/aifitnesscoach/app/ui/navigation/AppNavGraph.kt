package com.aifitnesscoach.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aifitnesscoach.app.data.repository.FitnessRepository
import com.aifitnesscoach.app.ui.ViewModelFactory
import com.aifitnesscoach.app.ui.editprofile.EditProfileScreen
import com.aifitnesscoach.app.ui.editprofile.EditProfileViewModel
import com.aifitnesscoach.app.ui.history.HistoryScreen
import com.aifitnesscoach.app.ui.history.HistoryViewModel
import com.aifitnesscoach.app.ui.home.HomeScreen
import com.aifitnesscoach.app.ui.home.HomeViewModel
import com.aifitnesscoach.app.ui.onboarding.OnboardingScreen
import com.aifitnesscoach.app.ui.onboarding.OnboardingViewModel
import com.aifitnesscoach.app.ui.progress.ProgressScreen
import com.aifitnesscoach.app.ui.progress.ProgressViewModel
import com.aifitnesscoach.app.ui.workout.WorkoutScreen
import com.aifitnesscoach.app.ui.workout.WorkoutViewModel

private object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val WORKOUT = "workout"
    const val HISTORY = "history"
    const val PROGRESS = "progress"
    const val EDIT_PROFILE = "edit_profile"
}

private data class BottomTab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

private val bottomTabs = listOf(
    BottomTab(Routes.HOME, "Home", Icons.Default.Home),
    BottomTab(Routes.HISTORY, "History", Icons.Default.History),
    BottomTab(Routes.PROGRESS, "Progress", Icons.Default.TrendingUp)
)

@Composable
fun AppNavGraph(repository: FitnessRepository, hasProfile: Boolean) {
    val navController = rememberNavController()
    val factory = ViewModelFactory(repository)
    val startDestination = if (hasProfile) Routes.HOME else Routes.ONBOARDING

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomTabs.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomTabs.forEach { tab ->
                        val selected = backStackEntry?.destination?.hierarchy?.any { it.route == tab.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.ONBOARDING) {
                val vm: OnboardingViewModel = viewModel(factory = factory)
                OnboardingScreen(
                    viewModel = vm,
                    onFinished = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }
            composable(Routes.HOME) {
                val vm: HomeViewModel = viewModel(factory = factory)
                HomeScreen(
                    viewModel = vm,
                    onStartWorkout = { navController.navigate(Routes.WORKOUT) },
                    onEditProfile = { navController.navigate(Routes.EDIT_PROFILE) }
                )
            }
            composable(Routes.WORKOUT) {
                val vm: WorkoutViewModel = viewModel(factory = factory)
                WorkoutScreen(
                    viewModel = vm,
                    onDone = { navController.popBackStack() }
                )
            }
            composable(Routes.HISTORY) {
                val vm: HistoryViewModel = viewModel(factory = factory)
                HistoryScreen(viewModel = vm)
            }
            composable(Routes.PROGRESS) {
                val vm: ProgressViewModel = viewModel(factory = factory)
                ProgressScreen(viewModel = vm)
            }
            composable(Routes.EDIT_PROFILE) {
                val vm: EditProfileViewModel = viewModel(factory = factory)
                EditProfileScreen(
                    viewModel = vm,
                    onSaved = { navController.popBackStack() },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
