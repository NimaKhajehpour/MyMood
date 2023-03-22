package com.nima.mymood.screens

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.util.Xml
import android.view.ContextThemeWrapper
import android.widget.DatePicker
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.nima.mymood.components.MenuItems
import com.nima.mymood.R
import com.nima.mymood.ThemeDataStore
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.navigation.Screens
import com.nima.mymood.ui.theme.MyMoodTheme
import com.nima.mymood.ui.theme.neutral
import com.nima.mymood.ui.theme.very_dissatisfied
import com.nima.mymood.ui.theme.very_satisfied
import com.nima.mymood.viewmodels.MenuViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.coroutines.coroutineContext

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel
) {

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()){
    }

    val context = LocalContext.current
    
    val themeDataStore = ThemeDataStore(context)
    
    val isDark = themeDataStore.getTheme.collectAsState(initial = false).value

    var showExportDialog by remember{
        mutableStateOf(false)
    }

    var showImportDialog by remember {
        mutableStateOf(false)
    }

    var showConfirmImport by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    val allDays = viewModel.getAllDays().collectAsState(initial = emptyList()).value
    val allEffects = viewModel.getAllEffects().collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        if (showExportDialog){
            AlertDialog(onDismissRequest = {  },
                title = {

                },
                text = {
                    Text(text = "The Exported XML file will be accessible from your downloads folder!")
                },
                icon = {
                    CircularProgressIndicator()
                },
                dismissButton = {},
                confirmButton = {}
            )
        }

        if (showConfirmImport) {
            AlertDialog(onDismissRequest = {
                if (!showImportDialog) {
                    showConfirmImport = false
                }
            },
                title = {
                    if (!showImportDialog) {
                        Text(text = "Delete Previous Data?")
                    }
                },
                text = {
                    if (!showImportDialog) {
                        Text(
                            text = "In order to import your data the previous saved data on database will be deleted forever." +
                                    " Do you want to proceed?"
                        )
                    } else {
                        Text(text = "Import in progress. Please wait until process is done!")
                    }
                },
                icon = {
                    if (!showImportDialog) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    } else {
                        CircularProgressIndicator()
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showConfirmImport = false
                        },
                        enabled = !showImportDialog
                    ) {
                        Text(text = "Cancel")
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    "MyMoodExported.xml"
                                ).isFile
                            ) {
                                showImportDialog = true
                                viewModel.deleteAllDays()
                                viewModel.getAllEffects()
                                val importFile = File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    "MyMoodExported.xml"
                                )
                                try {
                                    val fileIn = FileInputStream(importFile)

                                    val parserFactory = XmlPullParserFactory.newInstance()
                                    val parser = parserFactory.newPullParser()

                                    parser.setFeature(
                                        XmlPullParser.FEATURE_PROCESS_NAMESPACES,
                                        false
                                    )
                                    parser.setInput(fileIn, "UTF-8")

                                    var tag: String? = ""
                                    var event = parser.eventType

                                    while (event != XmlPullParser.END_DOCUMENT) {
                                        tag = parser.name
                                        when (event) {
                                            XmlPullParser.START_TAG -> {
                                                if (tag == "Day") {
                                                    val day = parser.getAttributeValue("", "day")
                                                    val month =
                                                        parser.getAttributeValue("", "month")
                                                    val year = parser.getAttributeValue("", "year")
                                                    val id = parser.getAttributeValue("", "id")
                                                    viewModel.addDay(
                                                        Day(
                                                            UUID.fromString(id),
                                                            day.toInt(),
                                                            month.toInt(),
                                                            year.toInt()
                                                        )
                                                    )
                                                }
                                                if (tag == "Effect") {
                                                    val description =
                                                        parser.getAttributeValue("", "description")
                                                    val rate = parser.getAttributeValue("", "rate")
                                                    val eId = parser.getAttributeValue("", "id")
                                                    val fk = parser.getAttributeValue("", "fk")
                                                    viewModel.addEffect(
                                                        Effect(
                                                            id = UUID.fromString(eId),
                                                            foreignKey = UUID.fromString(fk),
                                                            description = description,
                                                            rate = rate.toInt()
                                                        )
                                                    )
                                                }
                                            }
                                            XmlPullParser.END_TAG -> {
                                                if (tag == "MyMood") {
                                                    showImportDialog = false
                                                    showConfirmImport = false
                                                }
                                            }
                                        }
                                        event = parser.next()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    showImportDialog = false
                                    showConfirmImport = false
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Either the file (MyMoodExported.xml) does not exist in downloads directory or it has been renamed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        enabled = !showImportDialog
                    ) {
                        Text(text = "Confirm")
                    }
                }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            FilledIconButton(
                onClick = {
                    navController.popBackStack()
                },
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Home, contentDescription = null)
            }
            Spacer(modifier = Modifier.weight(1f))

            FilledIconButton(
                onClick = {
                    scope.launch { 
                        themeDataStore.saveTheme(!isDark!!)
                    }
                },
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                AnimatedContent(targetState = isDark) {
                    if (isDark == true){
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_light_mode_24),
                            contentDescription = null)
                    }else{
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_dark_mode_24),
                            contentDescription = null)
                    }
                }
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center,
            contentPadding = PaddingValues(16.dp)
        ){
            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_outline_sentiment_very_dissatisfied_24,
                    title = "Happy Effects",
                    tint = very_satisfied
                ) {
                    // go to happy effect
                    navController.navigate(Screens.HappyEffects.name)
                }
            }
            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_outline_sentiment_neutral_24,
                    title = "Neutral Effects",
                    tint = neutral
                ) {
                    // go to neutral effect
                    navController.navigate(Screens.NeutralEffects.name)

                }
            }
            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_outline_sentiment_very_dissatisfied_24,
                    title = "Sad Effects",
                    tint = very_dissatisfied
                ) {
                    // go to sad effect
                    navController.navigate(Screens.SadEffects.name)

                }
            }
            item {
                MenuItems(icon = Icons.Outlined.Search,
                    icon2 = null,
                    title = "Saved Days",
                    tint = Color.Black
                ) {
                    // go search
                    navController.navigate(Screens.SavedDays.name)
                }
            }

            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_baseline_arrow_upward_24,
                    title = "Export Data",
                    tint = Color.Black
                ) {
                    // action export
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) -> {
                            // do export
                            val exportedData = File(
                                Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS
                                ), "MyMoodExported.xml")
                            try {
                                exportedData.createNewFile()
                            }catch (e: Exception){
                                e.printStackTrace()
                            }
                            var fileOs: FileOutputStream? = null

                            try{
                                fileOs = FileOutputStream(exportedData)
                            }catch (e: Exception){
                                e.printStackTrace()
                            }

                            val xmlSerializer = Xml.newSerializer()

                            try{
                                scope.launch{
                                    showExportDialog = true
                                    xmlSerializer.setOutput(fileOs, "UTF-8")
                                    xmlSerializer.startDocument("", true)
                                    xmlSerializer.startTag("", "MyMood")
                                    for (day in allDays) {
                                        xmlSerializer.startTag("", "Day")
                                        xmlSerializer.attribute("", "day", "${day.day}")
                                        xmlSerializer.attribute("", "month", "${day.month}")
                                        xmlSerializer.attribute("", "year", "${day.year}")
                                        xmlSerializer.attribute("", "id", "${day.id.toString()}")
                                        val fk = day.id
                                        allEffects.filter {
                                            it.foreignKey == fk
                                        }.forEach { effect ->
                                            xmlSerializer.startTag("", "Effect")
                                            xmlSerializer.attribute(
                                                "",
                                                "description",
                                                effect.description
                                            )
                                            xmlSerializer.attribute(
                                                "",
                                                "rate",
                                                "${effect.rate}"
                                            )
                                            xmlSerializer.attribute(
                                                "",
                                                "id",
                                                "${effect.id}"
                                            )
                                            xmlSerializer.attribute(
                                                "",
                                                "fk",
                                                "${effect.foreignKey}"
                                            )

                                            xmlSerializer.endTag("", "Effect")
                                        }
                                        xmlSerializer.endTag("", "Day")
                                    }
                                    xmlSerializer.endTag("", "MyMood")
                                    xmlSerializer.endDocument()
                                    xmlSerializer.flush()
                                    withContext(Dispatchers.IO){
                                        fileOs?.close()
                                    }
                                    showExportDialog = false
                                    Toast.makeText(context, "File \"MyMoodExported.xml\" saved to downloads", Toast.LENGTH_LONG).show()
                                    }
                            }catch (e: Exception){
                                e.printStackTrace()
                                showExportDialog = false
                            }
                        }
                        else -> {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                )
                            )
                        }
                    }
                }
            }

            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_baseline_arrow_downward_24,
                    title = "Import Data",
                    tint = Color.Black
                ) {
                    // Action import

                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) -> {
                            // do import
                            showConfirmImport = true
                        }
                        else -> {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}