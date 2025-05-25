package com.nima.mymood.utils

import android.content.Context
import android.util.Log
import com.nima.mymood.ThemeDataStore
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import java.security.MessageDigest

fun hashPasscode(passcode: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(passcode.toByteArray(Charsets.UTF_8))
    return hashBytes.joinToString("") { "%02x".format(it) }
}

suspend fun verifyPasscode(context: Context, input: String): Boolean {
    val hashedInput = hashPasscode(input)
    val datastore = ThemeDataStore(context)
    val storedHash = datastore.getPasscode.first()
    return hashedInput == storedHash
}