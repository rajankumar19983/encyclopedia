package com.rajankumar.encyclopaedia.feature.importer

enum class OcrMetadataConfidence { NONE, PARTIAL, COMPLETE }

fun OcrSourceMetadata.confidence(): OcrMetadataConfidence = when {
  examName != null && year != null -> OcrMetadataConfidence.COMPLETE
  examName != null || year != null -> OcrMetadataConfidence.PARTIAL
  else -> OcrMetadataConfidence.NONE
}
