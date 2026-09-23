package com.rajankumar.encyclopaedia.feature.importer

enum class OcrQuestionLengthQuality { TOO_SHORT, NORMAL, VERY_LONG }

fun classifyOcrQuestionLength(text: String): OcrQuestionLengthQuality = when {
  text.trim().length < 5 -> OcrQuestionLengthQuality.TOO_SHORT
  text.trim().length > 2000 -> OcrQuestionLengthQuality.VERY_LONG
  else -> OcrQuestionLengthQuality.NORMAL
}
