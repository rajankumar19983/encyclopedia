package com.rajankumar.encyclopaedia.feature.importer

enum class OcrOptionCountQuality { TOO_FEW, NORMAL, UNUSUALLY_MANY }

fun classifyOcrOptionCount(count: Int): OcrOptionCountQuality = when {
  count < 2 -> OcrOptionCountQuality.TOO_FEW
  count <= 12 -> OcrOptionCountQuality.NORMAL
  else -> OcrOptionCountQuality.UNUSUALLY_MANY
}
