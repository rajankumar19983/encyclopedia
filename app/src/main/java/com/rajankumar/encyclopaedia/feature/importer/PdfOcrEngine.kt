package com.rajankumar.encyclopaedia.feature.importer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Renders PDF pages in memory and runs the same bundled ML Kit OCR used for images.
 * Rendered bitmaps are recycled immediately and are never persisted to storage.
 */
class PdfOcrEngine {
  suspend fun recognize(
    context: Context,
    uri: Uri,
    onProgress: (page: Int, total: Int) -> Unit = { _, _ -> }
  ): String {
    val descriptor = context.contentResolver.openFileDescriptor(uri, "r")
      ?: error("Unable to open PDF")

    descriptor.use { file ->
      PdfRenderer(file).use { renderer ->
        require(renderer.pageCount > 0) { "PDF has no pages" }
        val pages = ArrayList<String>(renderer.pageCount)
        for (index in 0 until renderer.pageCount) {
          onProgress(index + 1, renderer.pageCount)
          renderer.openPage(index).use { page ->
            // 2x rendering substantially improves small printed MCQ OCR while keeping
            // memory bounded because only one page bitmap exists at a time.
            val width = (page.width * 2).coerceAtMost(3000)
            val height = (page.height * 2).coerceAtMost(4200)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            try {
              page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
              pages += recognizeBitmap(bitmap)
            } finally {
              bitmap.recycle()
            }
          }
        }
        return pages.joinToString("\n")
      }
    }
  }

  private suspend fun recognizeBitmap(bitmap: Bitmap): String {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    return try {
      val image = InputImage.fromBitmap(bitmap, 0)
      suspendCancellableCoroutine { continuation ->
        recognizer.process(image)
          .addOnSuccessListener { result ->
            if (continuation.isActive) continuation.resume(result.text)
          }
          .addOnFailureListener { error ->
            if (continuation.isActive) continuation.resumeWithException(error)
          }
      }
    } finally {
      recognizer.close()
    }
  }
}
