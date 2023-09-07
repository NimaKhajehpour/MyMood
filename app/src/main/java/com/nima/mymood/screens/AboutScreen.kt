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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "MyMood",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 15.dp)
                )

                Badge {
                    Text(
                        text = "V${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Light,
                    )
                }
            }

            Text(
                text = "A simple and minimalistic mood tracking app to help you track what affected your mood and day.",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
            )

            Divider(
                thickness = 3.dp,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 32.dp)
            )

            Button(
                onClick = {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data = Uri.parse("https://www.github.com/NimaKhajehpour/MyMood")
                    context.startActivity(browserIntent)
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.github_mark),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(32.dp),
                    tint = Color.White
                )

                Text(text = "See App Repository")
            }

            Button(
                onClick = {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data = Uri.parse("https://www.github.com/NimaKhajehpour")
                    context.startActivity(browserIntent)
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.github_mark),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(32.dp),
                    tint = Color.White
                )

                Text(text = "Made by: Nima Khajehpour")
            }

            Button(
                onClick = {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data =
                        Uri.parse("https://www.github.com/NimaKhajehpour/MyMood/issues")
                    context.startActivity(browserIntent)
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 32.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_bug_report_24),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(32.dp)
                )

                Text(text = "Report a bug/Request features")
            }

            Button(
                onClick = {
                    val browserIntent = Intent(Intent.ACTION_VIEW)
                    browserIntent.data =
                        Uri.parse("https://t.me/+bwYZeynt5JNkMDdk")
                    context.startActivity(browserIntent)
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 32.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2AABEE)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.telegram_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .size(32.dp),
                    tint = Color.White
                )

                Text(text = "Join Telegram Group")
            }
        }
    }
}