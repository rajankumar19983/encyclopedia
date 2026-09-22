package com.rajankumar.encyclopaedia.feature.importer

fun importSourceLabel(source: String, metadata: OcrSourceMetadata): String =
  listOfNotNull(source.takeIf(String::isNotBlank), metadata.examName, metadata.year?.toString()).joinToString(" • ")
