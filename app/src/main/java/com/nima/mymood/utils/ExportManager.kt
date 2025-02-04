package com.nima.mymood.utils

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import org.xmlpull.v1.XmlSerializer
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ExportManager(private val context: Context) {

    fun exportData(permissionLauncher: ManagedActivityResultLauncher<String, Boolean>, allDays: List<Day>, allEffects: List<Effect>) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // Android 9 (API 28) and below - Request storage permission
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                    exportDataToFile(allDays, allEffects)
                }
                else -> {
                    permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        } else {
            // Android 10 (API 29) and above - No permission needed, use scoped storage or MediaStore
            exportDataToExternalStorage(allDays, allEffects)
        }
    }

    // Function for Android 9 and below (Exports to Downloads folder)
    private fun exportDataToFile(allDays: List<Day>, allEffects: List<Effect>) {
        val exportedData = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "MyMoodExportedV4.xml"
        )

        try {
            exportedData.createNewFile()
            FileOutputStream(exportedData).use { fileOs ->
                writeXmlData(fileOs, allDays, allEffects)
            }
            Toast.makeText(context, "File saved to Downloads", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Function for Android 10 and above (Uses app-specific storage)
    private fun exportDataToExternalStorage(allDays: List<Day>, allEffects: List<Effect>) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            // For Android 9 and below, save the file to public external storage (Downloads folder)
            val exportedData = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "MyMoodExportedV4.xml"
            )
            try {
                exportedData.createNewFile()
                FileOutputStream(exportedData).use { fileOs ->
                    writeXmlData(fileOs, allDays, allEffects)
                }
                Toast.makeText(context, "File saved to Downloads", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to save file", Toast.LENGTH_LONG).show()
            }
        } else {
            // For Android 10 (API 29) and above, use Scoped Storage (MediaStore API)
            val contentResolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "MyMoodExportedV4.xml")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/xml")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let { fileUri ->
                try {
                    contentResolver.openOutputStream(fileUri)?.use { fileOs ->
                        writeXmlData(fileOs, allDays, allEffects)
                    }
                    Toast.makeText(context, "File saved to Downloads", Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Failed to save file", Toast.LENGTH_LONG).show()
                }
            } ?: run {
                Toast.makeText(context, "Failed to create file", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Function to write XML data
    private fun writeXmlData(fileOs: OutputStream, allDays: List<Day>, allEffects: List<Effect>) {
        val xmlSerializer = org.xmlpull.v1.XmlPullParserFactory.newInstance().newSerializer()

        try {
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
                xmlSerializer.attribute("", "id", "${day.id}")

                allEffects.filter { it.foreignKey == day.id }.forEach { effect ->
                    xmlSerializer.startTag("", "Effect")
                    xmlSerializer.attribute("", "description", effect.description)
                    xmlSerializer.attribute("", "rate", "${effect.rate}")
                    xmlSerializer.attribute("", "id", "${effect.id}")
                    xmlSerializer.attribute("", "fk", "${effect.foreignKey}")
                    xmlSerializer.attribute("", "hour", "${effect.hour}")
                    xmlSerializer.attribute("", "minute", "${effect.minute}")
                    xmlSerializer.endTag("", "Effect")
                }

                xmlSerializer.endTag("", "Day")
            }

            xmlSerializer.endTag("", "MyMood")
            xmlSerializer.endDocument()
            xmlSerializer.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
