package com.nima.mymood.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nima.mymood.screens.MainScreen
import com.nima.mymood.screens.PinScreen

@Composable
fun MainNavigation(hasPasscode: Boolean) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = if (hasPasscode) MainScreens.PinScreen.name else MainScreens.MainScreen.name){
        composable(MainScreens.MainScreen.name){
            MainScreen()
        }
        composable(MainScreens.PinScreen.name){
            PinScreen(navController = navController)
        }
    }

}