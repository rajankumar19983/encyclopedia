package com.rajankumar.encyclopaedia.feature.importer

fun OcrSourceMetadata.displayParts(): List<String> = buildList {
  examName?.let(::add)
  year?.let { if (examDate == null) add(it.toString()) }
  examDate?.let(::add)
  shift?.let(::add)
}

fun OcrSourceMetadata.reviewLabel(): String = displayParts().joinToString(" • ").ifBlank { "Exam metadata not detected" }

fun OcrSourceMetadata.needsMetadataReview(): Boolean = confidence == OcrMetadataConfidence.LOW || confidence == OcrMetadataConfidence.NONE
