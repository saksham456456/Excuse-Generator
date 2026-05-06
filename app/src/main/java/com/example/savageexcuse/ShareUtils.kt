package com.example.savageexcuse

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

object ShareUtils {

    fun shareViewAsImage(context: Context, view: View, excuseText: String) {
        // Run compression off the main thread to prevent UI freezing
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val bitmap = getBitmapFromView(view)

            try {
                val cachePath = File(context.cacheDir, "images")
                cachePath.mkdirs()
                val file = File(cachePath, "savage_excuse_${System.currentTimeMillis()}.png")
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()

                val contentUri: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, contentUri)
                    putExtra(Intent.EXTRA_TEXT, "Generated with Savage App 🔥\n$excuseText")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }

                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                    context.startActivity(Intent.createChooser(shareIntent, "Share your savage excuse via..."))
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        view.draw(canvas)
        return bitmap
    }
}
