package com.nima.mymood.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.mymood.screens.AboutScreen
import com.nima.mymood.screens.DayGraphScreen
import com.nima.mymood.screens.DayScreen
import com.nima.mymood.screens.DonateScreen
import com.nima.mymood.screens.EditScreen
import com.nima.mymood.screens.HappyEffectsScreen
import com.nima.mymood.screens.HomeScreen
import com.nima.mymood.screens.MenuScreen
import com.nima.mymood.screens.NeutralEffectsScreen
import com.nima.mymood.screens.SadEffectsScreen
import com.nima.mymood.screens.SaveDaysScreen
import com.nima.mymood.screens.TodayMoodScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoodNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.name) {
        composable(Screens.HomeScreen.name) {
            HomeScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(
            Screens.TodayMoodScreen.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.StringType },
            )
        ) {
            TodayMoodScreen(
                navController = navController,
                viewModel = koinViewModel(),
                id = it.arguments?.getString("id"),
            )
        }

        composable(Screens.MenuScreen.name) {
            MenuScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.HappyEffects.name) {
            HappyEffectsScreen(navController = navController, viewModel = koinViewModel())
        }
        composable(Screens.NeutralEffects.name) {
            NeutralEffectsScreen(navController = navController, viewModel = koinViewModel())
        }
        composable(Screens.SadEffects.name) {
            SadEffectsScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.SavedDays.name) {
            SaveDaysScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.DayScreen.name + "/{id}",
            arguments = listOf(
                navArgument(name = "id") { type = NavType.StringType }
            )
        ) {
            DayScreen(
                navController = navController, viewModel = koinViewModel(),
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
                koinViewModel()
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
                viewModel = koinViewModel()
            )

        }
    }
}