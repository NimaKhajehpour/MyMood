package com.nima.mymood

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.nima.mymood.navigation.MoodNavigation
import com.nima.mymood.screens.AboutScreen
import com.nima.mymood.screens.MainScreen
import com.nima.mymood.ui.theme.MyMoodTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            val themeDataStore = ThemeDataStore(context)
            val theme = themeDataStore.getTheme.collectAsState(null).value
            if (theme != null) {
                runBlocking {
                    delay(1500)
                }
//                enableEdgeToEdge()
                MyMoodTheme(
                    darkTheme = theme,
                    dynamicColor = false
                ) {
                    val view = LocalView.current
                    val window = (view.context as Activity).window

                    SideEffect {
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !theme // negate darkTheme
                    }
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        MainScreen()
                    }
                }
            }else{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    AboutScreen(navController = rememberNavController())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyMoodTheme {
    }
}