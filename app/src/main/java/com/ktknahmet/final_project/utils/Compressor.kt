package com.ktknahmet.final_project.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.Options
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class Compressor {
    companion object {
        private var height: Int = 0
        private var width: Int = 0
        private var inSampleSize: Int = 0
        private var encodedfile: String = ""

        @Throws(IOException::class)
        fun compressImageFile(imageFile: File, reqHeight: Int, reqWidth: Int, filePath: String, quality: Int, compressFormat: CompressFormat, orientation: Int): File {
            var fileOutputStream: FileOutputStream? = null
            val file = (File(filePath)).parentFile
            if (file != null) {
                if (!file.exists()) {
                    file.mkdirs()
                }
            }
            try {
                fileOutputStream = FileOutputStream(filePath)
                decodeBitmapAndCompress(imageFile, reqHeight, reqWidth, orientation).compress(compressFormat, quality, fileOutputStream)
            } finally
            {
                if (fileOutputStream != null) {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                }
            }
            return File(filePath)
        }
        @Throws(IOException::class)
        fun decodeBitmapAndCompress(imageFile: File, reqHeight: Int, reqWidth: Int, reqOrientation: Int): Bitmap {
            val options = Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imageFile.absolutePath, options)
            options.inSampleSize = calculateSampleSize(options, reqHeight, reqWidth)
            options.inJustDecodeBounds = false
            var scaledBitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
            val exifInterface = ExifInterface(imageFile.absolutePath)
            val orientation = exifInterface.getAttributeInt("Orientation", 0)
            val matrix = Matrix()
            when (orientation) {
                6 -> {
                    matrix.postRotate(90.0f)
                }
                3 -> {
                    matrix.postRotate(180.0f)
                }
                8 -> {
                    matrix.postRotate(270.0f)
                }
            }
            if (reqOrientation > 0) {
                matrix.postRotate(reqOrientation.toFloat())
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
            return scaledBitmap
        }
        private fun calculateSampleSize(options: Options, reqHeight: Int, reqWidth: Int): Int {
            height = options.outHeight
            width = options.outWidth
            inSampleSize = 1
            val halfHeight = height / 2
            val halfWidth = width / 2
            if (height > reqHeight || width > reqWidth) {
                while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }
        fun getBase64forCompressedImage(compressFile: File): String {
            val fileInputStreamReader: FileInputStream?
            val bytes = ByteArray(compressFile.length().toInt())
            try {
                fileInputStreamReader = FileInputStream(compressFile)
                fileInputStreamReader.read(bytes)
                encodedfile = Base64.encodeToString(bytes, 0)
            } catch (var4: IOException) {
                var4.printStackTrace()
            }
            return encodedfile
        }
    }
}
