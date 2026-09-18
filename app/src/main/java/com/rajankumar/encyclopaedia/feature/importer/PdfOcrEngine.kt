package com.rajankumar.encyclopaedia.feature.importer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Processes every page in the selected PDF. There is deliberately no file-size or
 * page-count limit. Only one rendered page is kept in memory at a time, then recycled.
 * This makes very large PDFs practical without retaining source-page images.
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
        val output = StringBuilder()

        for (index in 0 until renderer.pageCount) {
          onProgress(index + 1, renderer.pageCount)
          renderer.openPage(index).use { page ->
            // No arbitrary pixel-dimension cap. Render at 2x native PDF page size
            // for better recognition of small printed text.
            val width = Math.multiplyExact(page.width, 2)
            val height = Math.multiplyExact(page.height, 2)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            try {
              page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
              if (output.isNotEmpty()) output.append('\n')
              output.append(recognizeBitmap(bitmap))
            } finally {
              bitmap.recycle()
            }
          }
        }

        return output.toString()
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
