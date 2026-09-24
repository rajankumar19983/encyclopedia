package com.rajankumar.encyclopaedia.feature.importer

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor

class PdfOcrExtractor(private val contentResolver: ContentResolver) {
  suspend fun extract(uri: Uri): OcrExtractionResult {
    val descriptor = contentResolver.openFileDescriptor(uri, "r")
      ?: return OcrExtractionResult(OcrSourceKind.PDF, listOf(OcrPageText(1, "", "Unable to open PDF.")))
    return try {
      extractDescriptor(descriptor)
    } finally {
      descriptor.close()
    }
  }

  private suspend fun extractDescriptor(descriptor: ParcelFileDescriptor): OcrExtractionResult =
    PdfRenderer(descriptor).use { renderer ->
      OfflineLatinTextRecognizer().use { recognizer ->
        val pages = ArrayList<OcrPageText>(renderer.pageCount)
        for (index in 0 until renderer.pageCount) {
          renderer.openPage(index).use { page ->
            val scale = 2
            val bitmap = Bitmap.createBitmap(page.width * scale, page.height * scale, Bitmap.Config.ARGB_8888)
            try {
              bitmap.eraseColor(Color.WHITE)
              page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
              val text = recognizer.recognize(bitmap)
              pages += OcrPageText(index + 1, text)
            } catch (error: Exception) {
              pages += OcrPageText(index + 1, "", error.message ?: "OCR failed on page ${index + 1}.")
            } finally {
              bitmap.recycle()
            }
          }
        }
        OcrExtractionResult(OcrSourceKind.PDF, pages)
      }
    }
}
