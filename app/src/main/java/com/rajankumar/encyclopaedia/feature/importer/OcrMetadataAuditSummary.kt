package com.rajankumar.encyclopaedia.feature.importer

fun OcrMetadataAudit.summary(): String = when {
  conflicts.isNotEmpty() -> "Conflicting exam metadata detected across OCR pages; verify the source."
  metadata.confidence == OcrMetadataConfidence.HIGH -> metadata.reviewLabel()
  metadata.confidence == OcrMetadataConfidence.MEDIUM -> "${metadata.reviewLabel()} • verify metadata"
  else -> "${metadata.reviewLabel()} • manual source verification recommended"
}
