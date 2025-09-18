package com.example.oralvis.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FileUtils {
    private const val APP_FOLDER = "OralVis" // folder name under Android/media/<package>/OralVis/Sessions/<id>

    private fun externalMediaDir(context: Context): File? {
        // Returns first external media dir (Android/media/<package>)
        return context.externalMediaDirs.firstOrNull()
    }

    fun getBaseDir(context: Context): File {
        val ext = externalMediaDir(context)
        return if (ext != null) File(ext, APP_FOLDER) else File(context.filesDir, APP_FOLDER)
    }

    fun getSessionDir(context: Context, sessionId: String): File {
        val sessionDir = File(getBaseDir(context), "Sessions/$sessionId")
        if (!sessionDir.exists()) sessionDir.mkdirs()
        return sessionDir
    }

    fun createImageFile(context: Context, sessionId: String): File {
        val dir = getSessionDir(context, sessionId)
        val stamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return File(dir, "IMG_${stamp}.jpg")
    }

    /**
     * If there was a temp session folder (temp_<ts>), move files to final session folder.
     * Returns list of absolute paths to moved files.
     */
    fun moveSessionFiles(context: Context, srcSessionId: String, destSessionId: String): List<String> {
        val src = File(getBaseDir(context), "Sessions/$srcSessionId")
        val dest = getSessionDir(context, destSessionId)
        val moved = mutableListOf<String>()
        if (src.exists()) {
            src.listFiles()?.forEach { f ->
                val destFile = File(dest, f.name)
                val ok = if (f.renameTo(destFile)) true else {
                    // fallback copy & delete
                    f.copyTo(destFile, overwrite = true)
                    f.delete()
                    true
                }
                if (ok) moved.add(destFile.absolutePath)
            }
            // remove src dir if empty
            src.delete()
        }
        return moved
    }
}
