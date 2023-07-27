package com.nima.mymood.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.mymood.screens.*

@Composable
fun MoodNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.name) {
        composable(Screens.HomeScreen.name) {
            HomeScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(
            Screens.TodayMoodScreen.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.StringType },
            )
        ) {
            TodayMoodScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                id = it.arguments?.getString("id"),
            )
        }

        composable(Screens.MenuScreen.name) {
            MenuScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(Screens.HappyEffects.name) {
            HappyEffectsScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(Screens.NeutralEffects.name) {
            NeutralEffectsScreen(navController = navController, viewModel = hiltViewModel())
        }
        composable(Screens.SadEffects.name) {
            SadEffectsScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(Screens.SavedDays.name) {
            SaveDaysScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(Screens.DayScreen.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.StringType }
            )
        ) {
            DayScreen(
                navController = navController, viewModel = hiltViewModel(),
                id = it.arguments?.getString("id")
            )
        }

        composable(Screens.AboutScreen.name) {
            AboutScreen(navController = navController)
        }

        composable(Screens.DayGraphScreen.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.StringType }
            )
        ) {
            DayGraphScreen(
                navController = navController,
                id = it.arguments?.getString("id"),
                hiltViewModel()
            )
        }

        composable(Screens.DonateScreen.name) {
            DonateScreen(
                navController = navController
            )
        }

        composable(Screens.EditScreen.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.StringType }
            )
        ) {
            EditScreen(
                navController = navController,
                id = it.arguments?.getString("id"),
                viewModel = hiltViewModel()
            )

        }
    }
}