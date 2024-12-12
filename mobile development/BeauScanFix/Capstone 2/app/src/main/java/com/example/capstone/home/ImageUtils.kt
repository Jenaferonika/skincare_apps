package com.example.capstone.home

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MAX_IMAGE_SIZE = 1000000
private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private val currentTimestamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())

fun generateImageUri(context: Context): Uri {
    var uri: Uri? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$currentTimestamp.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
        }
        uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }
    return uri ?: createImageUriForLegacy(context)
}

private fun createImageUriForLegacy(context: Context): Uri {
    val directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val imageFile = File(directory, "MyCamera/$currentTimestamp.jpg")
    if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdirs()

    return FileProvider.getUriForFile(
        context,
        "com.example.capstone.fileprovider",
        imageFile
    )
}

fun createTemporaryFile(context: Context): File {
    val cacheDir = context.externalCacheDir
    return File.createTempFile(currentTimestamp, ".jpg", cacheDir)
}

fun convertUriToFile(imageUri: Uri, context: Context): File {
    val tempFile = createTemporaryFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(tempFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) {
        outputStream.write(buffer, 0, length)
    }
    outputStream.close()
    inputStream.close()
    return tempFile
}

fun File.compressImageFile(): File {
    val file = this
    val bitmap = BitmapFactory.decodeFile(file.path).rotateImageIfNeeded(file)
    var compressQuality = 100
    var streamLength: Int

    do {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream)
        val bmpPicByteArray = outputStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAX_IMAGE_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

fun Bitmap.rotateImageIfNeeded(file: File): Bitmap? {
    val exif: ExifInterface = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ExifInterface(file)
    } else {
        ExifInterface(file.absolutePath)
    }

    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED
    )

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(this, 90F)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(this, 180F)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(this, 270F)
        ExifInterface.ORIENTATION_NORMAL -> this
        else -> this
    }
}

fun rotateBitmap(source: Bitmap, angle: Float): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height, matrix, true
    )
}
