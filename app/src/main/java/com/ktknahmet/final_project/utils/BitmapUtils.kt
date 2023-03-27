@file:Suppress("unused")

package com.ktknahmet.final_project.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.crazylegend.kotlinextensions.base64EncodeToString
import com.crazylegend.kotlinextensions.bitmap.decodeBitmap
import com.crazylegend.kotlinextensions.string.base64Decode
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

fun String.decodeBase64BitmapScaled(): Bitmap {
    val byteArray: ByteArray = this.base64Decode()
    val bitmap: Bitmap = byteArray.decodeBitmap(0, byteArray.size)!!
    return Bitmap.createScaledBitmap(bitmap, 300, 300, false)
}

fun String.decodeBase64BitmapScaled2(): Bitmap {
    val byteArray: ByteArray = this.base64Decode()
    val bitmap: Bitmap = byteArray.decodeBitmap(0, byteArray.size)!!
    return Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, false)
}

fun String.decodeBase64BitmapRounded(): RoundedBitmapDrawable {
    return RoundedBitmapDrawableFactory.create(appCtx.resources, this.decodeBase64BitmapScaled())
}

fun File.compress(context: Context): File? {
    var compressedFile: File? = null
    try {
        compressedFile =
            CompressImage(context).setQuality(30).setMaxHeight(100).setCompressFormat(Bitmap.CompressFormat.JPEG).compressToFile(this)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return compressedFile
}

fun Bitmap.toFile(photoName: String): Uri {
    // Get the context wrapper
    val wrapper = ContextWrapper(appCtx)

    // Initialize a new file instance to save bitmap object
    var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
    file = File(file, photoName)

    try {
        // Compress the bitmap and save in jpg format
        val stream: OutputStream = FileOutputStream(file)
        this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // Return the saved bitmap uri
    return Uri.parse(file.absolutePath)
}

fun Bitmap.decodeBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val b = byteArrayOutputStream.toByteArray()
    return b.base64EncodeToString()
}
