package com.nima.mymood

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.nima.mymood.navigation.MoodNavigation
import com.nima.mymood.ui.theme.MyMoodTheme
import kotlinx.coroutines.flow.collectIndexed

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val context = LocalContext.current

            val themeDataStore = ThemeDataStore(context)

            MyMoodTheme(
                useDarkTheme = themeDataStore.getTheme.collectAsState(initial = false).value!!,
                useDynamicColors =   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    themeDataStore.getMaterialYou.collectAsState(initial = false).value!! else false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoodNavigation()
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