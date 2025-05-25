package com.nima.mymood.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nima.mymood.components.MoodNavigationDrawerItem
import com.nima.mymood.navigation.MoodNavigation
import com.nima.mymood.navigation.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedDrawer by remember { mutableStateOf<String?>(null) }

    // Listen for navigation changes and update currentRoute
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            selectedDrawer = backStackEntry.destination.route
        }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column (
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ){
                    Spacer(modifier = Modifier.height(16.dp))
                    MoodNavigationDrawerItem(
                        label = "All Effects",
                        selected = selectedDrawer == "AllEffectsScreen"
                    ){
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screens.AllEffectsScreen.name){
                            launchSingleTop = true
                        }

                    }
                    MoodNavigationDrawerItem(
                        label = "All Days",
                        selected = selectedDrawer == "SavedDays",
                    ){
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screens.SavedDays.name){
                            launchSingleTop = true
                        }
                    }
                    MoodNavigationDrawerItem(
                        label = "Compare Days",
                        selected = selectedDrawer == "DayCompareScreen",
                    ){
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screens.DayCompareScreen.name){
                            launchSingleTop = true
                        }
                    }
                    MoodNavigationDrawerItem(
                        label = "Graph Overview",
                        selected = selectedDrawer == "DaysGraphOverViewScreen",
                    ){
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screens.DaysGraphOverViewScreen.name){
                            launchSingleTop = true
                        }
                    }
                    MoodNavigationDrawerItem(
                        label = "Donate",
                        selected = selectedDrawer == "DonateScreen",
                    ){
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screens.DonateScreen.name){
                            launchSingleTop = true
                        }
                    }
                    MoodNavigationDrawerItem(
                        label = "About",
                        selected = selectedDrawer == "AboutScreen",
                    ){
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate(Screens.AboutScreen.name){
                            launchSingleTop = true
                        }
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
        drawerState = drawerState,
        scrimColor = MaterialTheme.colorScheme.scrim,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text("MyMood", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleLarge)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isOpen){
                                        drawerState.close()
                                    }else{
                                        drawerState.open()
                                    }
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = Color.Transparent
                            )
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navController.navigate(Screens.SettingsScreen.name){
                                    launchSingleTop = true
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface,
                                containerColor = Color.Transparent)
                        ) {
                                Icon(Icons.Default.Settings, contentDescription = null)
                            }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        scrolledContainerColor = MaterialTheme.colorScheme.surface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    ),
                )
            }
        ){ paddingValues ->
            Surface (modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()){
                MoodNavigation(navController)
            }
        }
    }
}