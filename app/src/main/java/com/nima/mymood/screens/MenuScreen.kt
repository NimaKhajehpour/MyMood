package com.nima.mymood.screens

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.ui.semantics.Role
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
    val useDynamicColors = themeDataStore.getMaterialYou.collectAsState(initial = false).value

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

    var showThemeSelectDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        if (showThemeSelectDialog){
            AlertDialog(onDismissRequest = {
                showThemeSelectDialog = false
            },
                confirmButton = {
                    TextButton(onClick = {
                        showThemeSelectDialog = false
                    }) {
                        Text(text = "Confirm")
                    }
                },
                icon = {
                    Icon(painter = painterResource(id = R.drawable.baseline_palette_24), contentDescription = null)
                },
                title ={
                    Text(text = "Theme Select")
                },
                shape = RoundedCornerShape(15.dp),
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                Button(
                                    onClick = {
                                        scope.launch {
                                            themeDataStore.saveTheme(true)
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .height(120.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                    ),
                                    border = if (isDark == true) BorderStroke(2.dp, color = Color.Cyan) else null
                                ) {
                                    Text(text = "Dark Theme")
                                }
                                Button(
                                    onClick = {
                                        scope.launch {
                                            themeDataStore.saveTheme(false)
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .height(120.dp),
                                    shape = RoundedCornerShape(15.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = Color.Black
                                    ),
                                    border = if (isDark == false) BorderStroke(2.dp, color = Color.Cyan) else null
                                ) {
                                    Text(text = "Light Theme")
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .selectable(
                                        selected = useDynamicColors!!,
                                        role = Role.Checkbox
                                    ) {
                                        scope.launch {
                                            themeDataStore.saveMaterialYou(!useDynamicColors)
                                        }
                                    },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Checkbox(checked = useDynamicColors!!, onCheckedChange = {
                                    scope.launch {
                                        themeDataStore.saveMaterialYou(!useDynamicColors)
                                    }
                                })
                                Text(text = "Dynamic Colors")
                            }
                        }
                    }
                }
            )
        }

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
                                    "\nWith the newest version of this app, importing the data from old files will not be possible because of the new structure of the exported file. The new exported data is saved under the name: MyMoodExportedV3.xml" +
                                    "\nDo you want to proceed?"
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
                                    "MyMoodExportedV3.xml"
                                ).isFile
                            ) {
                                showImportDialog = true
                                viewModel.deleteAllDays()
                                viewModel.getAllEffects()
                                val importFile = File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    "MyMoodExportedV3.xml"
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
                                                    val red = parser.getAttributeValue("", "red")
                                                    val green = parser.getAttributeValue("", "green")
                                                    val blue = parser.getAttributeValue("", "blue")
                                                    val rate = parser.getAttributeValue("", "rate")
                                                    viewModel.addDay(
                                                        Day(
                                                            UUID.fromString(id),
                                                            day.toInt(),
                                                            month.toInt(),
                                                            year.toInt(),
                                                            red,
                                                            green,
                                                            blue,
                                                            rate
                                                        )
                                                    )
                                                }
                                                if (tag == "Effect") {
                                                    val description =
                                                        parser.getAttributeValue("", "description")
                                                    val rate = parser.getAttributeValue("", "rate")
                                                    val eId = parser.getAttributeValue("", "id")
                                                    val fk = parser.getAttributeValue("", "fk")
                                                    val hour = parser.getAttributeValue("", "hour")
                                                    val minute = parser.getAttributeValue("", "minute")
                                                    viewModel.addEffect(
                                                        Effect(
                                                            id = UUID.fromString(eId),
                                                            foreignKey = UUID.fromString(fk),
                                                            description = description,
                                                            rate = rate.toInt(),
                                                            hour = hour,
                                                            minute = minute
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
                                    "Either the file (MyMoodExportedV3.xml) does not exist in downloads directory or it has been renamed",
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
            horizontalArrangement = Arrangement.SpaceEvenly
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

            FilledIconButton(
                onClick = {
                          // go to donate page
                          navController.navigate(Screens.DonateScreen.name)
                },
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Outlined.Favorite, contentDescription = null)
            }

            FilledIconButton(onClick = {
                navController.navigate(Screens.AboutScreen.name)
            },
                shape = CircleShape,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
            }


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S){
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
            else{
                FilledIconButton(onClick = {
                    // show theme select dialog
                    showThemeSelectDialog = true
                },
                    shape = CircleShape,
                    modifier = Modifier.padding(16.dp)
                    ) {
                    Icon(painter = painterResource(id = R.drawable.baseline_palette_24), contentDescription = null)
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
                    icon2 = R.drawable.ic_outline_sentiment_very_satisfied_24,
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
                    tint = MaterialTheme.colorScheme.tertiary
                ) {
                    // go search
                    navController.navigate(Screens.SavedDays.name)
                }
            }

            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_baseline_arrow_upward_24,
                    title = "Export Data",
                    tint = MaterialTheme.colorScheme.tertiary
                ) {
                    // action export
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU){
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ) -> {
                                // do export
                                val exportedData = File(
                                    Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_DOWNLOADS
                                    ), "MyMoodExportedV3.xml"
                                )
                                try {
                                    exportedData.createNewFile()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                var fileOs: FileOutputStream? = null

                                try {
                                    fileOs = FileOutputStream(exportedData)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                val xmlSerializer = Xml.newSerializer()

                                try {
                                    scope.launch {
                                        showExportDialog = true
                                        xmlSerializer.setOutput(fileOs, "UTF-8")
                                        xmlSerializer.startDocument("", true)
                                        xmlSerializer.startTag("", "MyMood")
                                        for (day in allDays) {
                                            xmlSerializer.startTag("", "Day")
                                            xmlSerializer.attribute("", "day", "${day.day}")
                                            xmlSerializer.attribute("", "month", "${day.month}")
                                            xmlSerializer.attribute("", "year", "${day.year}")
                                            xmlSerializer.attribute("", "red", "${day.red}")
                                            xmlSerializer.attribute("", "green", "${day.green}")
                                            xmlSerializer.attribute("", "blue", "${day.blue}")
                                            xmlSerializer.attribute("", "rate", "${day.rate}")
                                            xmlSerializer.attribute(
                                                "",
                                                "id",
                                                "${day.id.toString()}"
                                            )
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
                                                xmlSerializer.attribute(
                                                    "",
                                                    "hour",
                                                    "${effect.hour}"
                                                )
                                                xmlSerializer.attribute(
                                                    "",
                                                    "minute",
                                                    "${effect.minute}"
                                                )

                                                xmlSerializer.endTag("", "Effect")
                                            }
                                            xmlSerializer.endTag("", "Day")
                                        }
                                        xmlSerializer.endTag("", "MyMood")
                                        xmlSerializer.endDocument()
                                        xmlSerializer.flush()
                                        withContext(Dispatchers.IO) {
                                            fileOs?.close()
                                        }
                                        showExportDialog = false
                                        Toast.makeText(
                                            context,
                                            "File \"MyMoodExportedV3.xml\" saved to downloads",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } catch (e: Exception) {
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
                    }else{
                        val exportedData = File(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_DOWNLOADS
                            ), "MyMoodExportedV3.xml"
                        )
                        try {
                            exportedData.createNewFile()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        var fileOs: FileOutputStream? = null

                        try {
                            fileOs = FileOutputStream(exportedData)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        val xmlSerializer = Xml.newSerializer()

                        try {
                            scope.launch {
                                showExportDialog = true
                                xmlSerializer.setOutput(fileOs, "UTF-8")
                                xmlSerializer.startDocument("", true)
                                xmlSerializer.startTag("", "MyMood")
                                for (day in allDays) {
                                    xmlSerializer.startTag("", "Day")
                                    xmlSerializer.attribute("", "day", "${day.day}")
                                    xmlSerializer.attribute("", "month", "${day.month}")
                                    xmlSerializer.attribute("", "year", "${day.year}")
                                    xmlSerializer.attribute("", "red", "${day.red}")
                                    xmlSerializer.attribute("", "green", "${day.green}")
                                    xmlSerializer.attribute("", "blue", "${day.blue}")
                                    xmlSerializer.attribute("", "rate", "${day.rate}")
                                    xmlSerializer.attribute(
                                        "",
                                        "id",
                                        "${day.id.toString()}"
                                    )
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
                                        xmlSerializer.attribute(
                                            "",
                                            "hour",
                                            "${effect.hour}"
                                        )
                                        xmlSerializer.attribute(
                                            "",
                                            "minute",
                                            "${effect.minute}"
                                        )

                                        xmlSerializer.endTag("", "Effect")
                                    }
                                    xmlSerializer.endTag("", "Day")
                                }
                                xmlSerializer.endTag("", "MyMood")
                                xmlSerializer.endDocument()
                                xmlSerializer.flush()
                                withContext(Dispatchers.IO) {
                                    fileOs?.close()
                                }
                                showExportDialog = false
                                Toast.makeText(
                                    context,
                                    "File \"MyMoodExportedV3.xml\" saved to downloads",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            showExportDialog = false
                        }
                    }
                }
            }

            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_baseline_arrow_downward_24,
                    title = "Import Data",
                    tint = MaterialTheme.colorScheme.tertiary
                ) {
                    // Action import

                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU){
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
                    else{
                        showConfirmImport = true
                    }
                }
            }

            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.ic_baseline_ssid_chart_24,
                    title = "Compare Days",
                    tint = MaterialTheme.colorScheme.tertiary
                ) {
                    // go to day compare
                    navController.navigate(Screens.DayCompareScreen.name)
                }
            }

            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.baseline_show_chart_24,
                    title = "Days Graph Overview",
                    tint = MaterialTheme.colorScheme.tertiary
                ) {
                    // go to days overview
                    navController.navigate(Screens.DaysGraphOverViewScreen.name)
                }
            }
            item {
                MenuItems(icon = null,
                    icon2 = R.drawable.baseline_calendar_month_24,
                    title = "Days Calendar Overview",
                    tint = MaterialTheme.colorScheme.tertiary
                ) {
                    // go to days overview
                    navController.navigate(Screens.DaysCalendarOverView.name)
                }
            }

        }
    }
}