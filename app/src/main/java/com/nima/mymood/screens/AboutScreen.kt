package com.nima.mymood.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.BuildConfig
import com.nima.mymood.R
import com.nima.mymood.utils.Constants
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
){

    val context = LocalContext.current as Activity

    Box{

        Column (
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "MyMood",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Write down Your mood changes, it will help!",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Column (
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW)
                        browserIntent.data = Uri.parse("https://www.github.com/NimaKhajehpour")
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.github_mark),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp),
                    )
                }

                IconButton(
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW)
                        browserIntent.data =
                            Uri.parse("https://t.me/+bwYZeynt5JNkMDdk")
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 32.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.telegram_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp),
                    )
                }

                IconButton(
                    onClick = {
                        val browserIntent = Intent(Intent.ACTION_VIEW)
                        browserIntent.data =
                            Uri.parse("https://discord.gg/6fq6MvX3fG")
                        context.startActivity(browserIntent)
                    },
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 32.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.discord_icon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp),
                    )
                }
            }

            Text(
                text = "Version ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}