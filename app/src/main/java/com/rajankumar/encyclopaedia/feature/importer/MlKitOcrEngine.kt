package com.rajankumar.encyclopaedia.feature.importer

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

class MlKitOcrEngine {
  suspend fun recognize(context: Context, uri: Uri): String {
    val image = InputImage.fromFilePath(context, uri)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    return try {
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
