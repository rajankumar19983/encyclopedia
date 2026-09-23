package com.rajankumar.encyclopaedia.feature.importer

fun sourceMetadataSummary(metadata: OcrSourceMetadata): String = when {
  metadata.examName != null && metadata.year != null -> "${metadata.examName} • ${metadata.year}"
  metadata.examName != null -> metadata.examName
  metadata.year != null -> metadata.year.toString()
  else -> "Source metadata not detected"
}
