package com.startars.smart_healthy_wallet.utils

import android.content.Context
import android.os.Environment
import java.io.File

fun getLocalModelPath(context: Context, fileName: String): String? {
    // 1. Point to the internal cache directory where native C++ has full access
    val internalFile = File(context.cacheDir, fileName)

    // If it's already copied, return the internal path immediately
    if (internalFile.exists()) {
        return internalFile.absolutePath
    }

    // 2. Resolve the public download directory correctly
    val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val sourceFile = File(downloadDir, fileName)

    if (!sourceFile.exists()) {
        // Handle error: User hasn't put the model in their Downloads folder yet
        return null
    }

    // 3. Stream the file into internal storage
    sourceFile.inputStream().use { input ->
        internalFile.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    return internalFile.absolutePath
}
