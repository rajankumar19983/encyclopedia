package com.rajankumar.encyclopaedia.feature.importer

fun OcrSourceMetadata.mergeWith(other: OcrSourceMetadata): OcrSourceMetadata = OcrSourceMetadata(
  examName = examName ?: other.examName,
  year = year ?: other.year,
  examDate = examDate ?: other.examDate,
  shift = shift ?: other.shift,
  confidence = maxOf(confidence, other.confidence)
)

fun extractMetadataAcrossPages(pages: List<OcrPageText>): OcrSourceMetadata = pages
  .asSequence()
  .filter { it.error == null }
  .map { OcrSourceMetadataExtractor.extract(it.text) }
  .fold(OcrSourceMetadata()) { accumulated, next -> accumulated.mergeWith(next) }
