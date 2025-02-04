package com.nima.mymood.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Xml
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.ThemeDataStore
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import com.nima.mymood.utils.ExportManager
import com.nima.mymood.utils.importDataFromUri
import com.nima.mymood.viewmodels.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID

private lateinit var exportManager: ExportManager

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val themeDataStore = ThemeDataStore(context)
    val isDark = themeDataStore.getTheme.collectAsState(false).value

    var isGrantedExport by remember {
        mutableStateOf(false)
    }

    val allDays = viewModel.getAllDays().collectAsState(initial = emptyList()).value
    val allEffects = viewModel.getAllEffects().collectAsState(initial = emptyList()).value

    val permissionLauncherExport = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            isGrantedExport = true
        } else {
            isGrantedExport = false
            Toast.makeText(context, "Storage permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) {
        it.let { importDataFromUri(context, it!!,
            addDay = {
                viewModel.addDay(it)
            },
            addEffect = {
                viewModel.addEffect(it)
            },
            deleteEffects = {
                viewModel.deleteAllEffects()
            },
            deleteDays = {
                viewModel.deleteAllDays()
            }
            ) }
    }

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
            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                onClick = {
                    importLauncher.launch(arrayOf("text/xml"))
                }
            ) {
                Text("Import Data")
            }
            Button(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 32.dp),
                onClick = {
                    exportManager = ExportManager(context)
                    exportManager.exportData(permissionLauncherExport, allDays, allEffects)
                }
            ) {
                Text("Export Data")
            }
        }
    }
}