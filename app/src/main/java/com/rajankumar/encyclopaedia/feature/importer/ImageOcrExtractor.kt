package com.rajankumar.encyclopaedia.feature.importer

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri

class ImageOcrExtractor(private val contentResolver: ContentResolver) {
  suspend fun extract(uri: Uri): OcrExtractionResult = OfflineLatinTextRecognizer().use { recognizer ->
    val bitmap = contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream)
      ?: return@use OcrExtractionResult(OcrSourceKind.IMAGE, listOf(OcrPageText(1, "", "Unable to decode image.")))
    try {
      val text = recognizer.recognize(bitmap)
      OcrExtractionResult(OcrSourceKind.IMAGE, listOf(OcrPageText(1, text)))
    } catch (error: Exception) {
      OcrExtractionResult(OcrSourceKind.IMAGE, listOf(OcrPageText(1, "", error.message ?: "OCR failed.")))
    } finally {
      bitmap.recycle()
    }
  }
}
