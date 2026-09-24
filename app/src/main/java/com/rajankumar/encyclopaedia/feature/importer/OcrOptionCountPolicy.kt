package com.rajankumar.encyclopaedia.feature.importer

data class OcrOptionCountPolicy(
  val supported: Boolean,
  val warning: String? = null
)

fun ParsedQuestionDraft.optionCountPolicy(): OcrOptionCountPolicy = when (options.size) {
  in 2..6 -> OcrOptionCountPolicy(true)
  0, 1 -> OcrOptionCountPolicy(false, "At least two options are required.")
  else -> OcrOptionCountPolicy(false, "More than six options were detected; review OCR structure before saving.")
}
