package com.nima.mymood.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.mymood.screens.AboutScreen
import com.nima.mymood.screens.AllEffectsScreen
import com.nima.mymood.screens.DayCompareScreen
import com.nima.mymood.screens.DayScreen
import com.nima.mymood.screens.DaySettingsScreen
//import com.nima.mymood.screens.DaysCalendarOverView
import com.nima.mymood.screens.DaysGraphOverviewScreen
import com.nima.mymood.screens.DonateScreen
import com.nima.mymood.screens.EditScreen
import com.nima.mymood.screens.HomeScreen
import com.nima.mymood.screens.SaveDaysScreen
import com.nima.mymood.screens.SettingsScreen
import com.nima.mymood.screens.TodayMoodScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoodNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.HomeScreen.name) {

        composable(
            Screens.HomeScreen.name
        ){
            HomeScreen(navController = navController, viewModel=koinViewModel())
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

        composable(Screens.DayCompareScreen.name){
            DayCompareScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.DaysGraphOverViewScreen.name){
            DaysGraphOverviewScreen(navController = navController, viewModel = koinViewModel())
        }

//        composable(Screens.DaysCalendarOverView.name){
//            DaysCalendarOverView(navController = navController, viewModel = koinViewModel())
//        }
        composable(Screens.AllEffectsScreen.name) {
            AllEffectsScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.DaySettingsScreen.name+"/{id}",
            arguments = listOf(
                navArgument(name = "id"){type = NavType.StringType}
            )
            ){
            DaySettingsScreen(
                navController,
                koinViewModel(),
                it.arguments?.getString("id")
            )
        }


        composable(Screens.SettingsScreen.name){
            SettingsScreen(navController = navController, viewModel = koinViewModel())
        }
    }
}