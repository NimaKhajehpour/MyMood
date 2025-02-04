package com.nima.mymood.utils

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.nima.mymood.model.Day
import com.nima.mymood.model.Effect
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream
import java.util.UUID

fun importDataFromUri(context: Context, uri: Uri, addDay: (Day) -> Unit, addEffect: (Effect) -> Unit, deleteDays: () -> Unit, deleteEffects: () -> Unit) {
    try {
        context.contentResolver.openInputStream(uri)?.use { fileIn ->
            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(fileIn, "UTF-8")

            var tag: String?
            var event = parser.eventType
            deleteDays()
            deleteEffects()
            while (event != XmlPullParser.END_DOCUMENT) {
                tag = parser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                        when (tag) {
                            "Day" -> {
                                val day = parser.getAttributeValue("", "day").toInt()
                                val month = parser.getAttributeValue("", "month").toInt()
                                val year = parser.getAttributeValue("", "year").toInt()
                                val id = UUID.fromString(parser.getAttributeValue("", "id"))
                                val red = parser.getAttributeValue("", "red")
                                val green = parser.getAttributeValue("", "green")
                                val blue = parser.getAttributeValue("", "blue")
                                val rate = parser.getAttributeValue("", "rate")
                                addDay(Day(id, day, month, year, red, green, blue, rate))
                            }
                            "Effect" -> {
                                val description = parser.getAttributeValue("", "description")
                                val rate = parser.getAttributeValue("", "rate").toInt()
                                val eId = UUID.fromString(parser.getAttributeValue("", "id"))
                                val fk = UUID.fromString(parser.getAttributeValue("", "fk"))
                                val hour = parser.getAttributeValue("", "hour")
                                val minute = parser.getAttributeValue("", "minute")
                                addEffect(Effect(eId, fk, description, rate, hour, minute))
                            }
                        }
                    }
                }
                event = parser.next()
            }
            Toast.makeText(context, "Import successful!", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error during import.", Toast.LENGTH_LONG).show()
    }
}
