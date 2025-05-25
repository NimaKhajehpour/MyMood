package com.nima.mymood.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.ThemeDataStore
import com.nima.mymood.components.NumberKeyBoard
import com.nima.mymood.navigation.MainScreens
import com.nima.mymood.utils.hashPasscode
import com.nima.mymood.utils.verifyPasscode
import kotlinx.coroutines.launch

@Composable
fun PinScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var passcode by remember {
        mutableStateOf("")
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            for (dots in 1..passcode.length){
                Text("*", modifier = Modifier.padding(end = 10.dp), style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.ExtraBold)
            }
        }
        Text("Enter Your Passcode")
        NumberKeyBoard {
            if (passcode.length < 4){
                if (it == "del"){
                    if (passcode.isNotEmpty()){
                        passcode = passcode.substring(0..passcode.length-2)
                    }
                }else if (it == "Ok"){
                }else {
                    passcode += it
                }
            }else if (passcode.length == 4){
                if (it == "del"){
                    if (passcode.isNotEmpty()){
                        passcode = passcode.substring(0..passcode.length-2)
                    }
                }else if (it == "Ok"){
                    scope.launch {
                        val verified = verifyPasscode(context = context, input = passcode)
                        if (verified){
                            navController.navigate(MainScreens.MainScreen.name){
                                popUpTo(MainScreens.PinScreen.name){
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }else{
                            passcode = ""
                        }
                    }
                }
            }
        }
    }

}