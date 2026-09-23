package com.rajankumar.encyclopaedia.feature.importer

data class OcrSourceRetentionPolicy(
  val retainOriginalMedia: Boolean = false,
  val retainExtractedTextDuringReview: Boolean = true,
  val persistExtractedTextAfterImport: Boolean = false,
)

val defaultOcrSourceRetentionPolicy = OcrSourceRetentionPolicy()
