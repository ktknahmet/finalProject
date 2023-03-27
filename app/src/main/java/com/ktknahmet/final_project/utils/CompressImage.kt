package com.ktknahmet.final_project.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.File
import java.io.IOException

@Suppress("unused")
class CompressImage(context: Context) {
    private var maxWidth = 612
    private var maxHeight = 816
    private var quality = 80
    private var orientation = 0
    private var compressFormat: Bitmap.CompressFormat
    private var destinationDirectory: String
    init {
        this.compressFormat = Bitmap.CompressFormat.JPEG
        this.destinationDirectory = context.cacheDir.path + File.separator + "images"
    }
    fun setMaxWidth(maxWidth: Int): CompressImage {
        this.maxWidth = maxWidth
        return this
    }
    fun setMaxHeight(maxHeight: Int): CompressImage {
        this.maxHeight = maxHeight
        return this
    }
    fun setQuality(quality: Int): CompressImage {
        this.quality = quality
        return this
    }
    fun setOrientation(orientation: Int): CompressImage {
        this.orientation = orientation
        return this
    }
    fun setCompressFormat(compressFormat: Bitmap.CompressFormat): CompressImage {
        this.compressFormat = compressFormat
        return this
    }
    @Throws(IOException::class)
    fun compressToBitmap(imageFile: File): Bitmap {
        return Compressor.decodeBitmapAndCompress(imageFile, this.maxHeight, this.maxWidth, this.orientation)
    }
    @Throws(IOException::class)
    fun compressToFile(imageFile: File): File {
        return this.compressToFile(imageFile, imageFile.name, this.orientation)
    }
    @Throws(IOException::class)
    fun compressToFile(imageFile: File, fileName: String, orientation: Int): File {
        return Compressor.compressImageFile(imageFile, this.maxHeight, this.maxWidth, this.destinationDirectory + File.separator + fileName, this.quality, this.compressFormat, orientation)
    }
    companion object {
        fun getBase64forImage(compressFile: File): String {
            return Compressor.getBase64forCompressedImage(compressFile)
        }
        fun decodeBase64(base64: String): Bitmap {
            val decodedBytes = Base64.decode(base64, 0)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        }
    }
}
