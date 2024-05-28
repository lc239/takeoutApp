package com.example.takeoutapplication.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.FileUtils
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream
import kotlin.random.Random

fun uriToFile(context: Context, uri: Uri): File?{
    if (uri.scheme == ContentResolver.SCHEME_FILE)
        return File(requireNotNull(uri.path))
    else if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        //把文件保存到沙盒
        val contentResolver = context.contentResolver
        val displayName = "${System.currentTimeMillis()}${Random.nextInt(0, 9999)}.${
            MimeTypeMap.getSingleton()
                .getExtensionFromMimeType(contentResolver.getType(uri))
        }"
        val ios = contentResolver.openInputStream(uri)
        if (ios != null) {
            return File("${context.cacheDir.absolutePath}/$displayName")
                .apply {
                    val fos = FileOutputStream(this)
                    FileUtils.copy(ios, fos)
                    fos.close()
                    ios.close()
                }
        } else return null
    }else return null
}