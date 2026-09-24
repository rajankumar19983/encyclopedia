package com.rajankumar.encyclopaedia.feature.importer

data class OcrMetadataConflict(
  val field: String,
  val values: Set<String>,
)

data class OcrMetadataReviewNotice(val message: String)

data class OcrMetadataAudit(
  val metadata: OcrSourceMetadata,
  val conflicts: List<OcrMetadataConflict>,
  val notices: List<OcrMetadataReviewNotice>,
) {
  val requiresReview: Boolean get() = conflicts.isNotEmpty() || metadata.needsMetadataReview()
}

data class OcrMetadataCandidate(
  val metadata: OcrSourceMetadata,
  val sourcePreview: String,
)

data class OcrMetadataReview(val confidence: OcrMetadataConfidence, val message: String)

fun detectOcrMetadataCandidate(text: String): OcrMetadataCandidate {
  val metadata = OcrSourceMetadataExtractor.extract(text)
  val preview = text.lineSequence()
    .map(String::trim)
    .filter(String::isNotBlank)
    .take(5)
    .joinToString(" ")
    .take(300)
  return OcrMetadataCandidate(metadata, preview)
}

fun detectMetadataConflicts(pages: List<OcrPageText>): List<OcrMetadataConflict> {
  val metadata = pages.filter { it.error == null }.map { OcrSourceMetadataExtractor.extract(it.text) }
  fun conflict(field: String, values: List<String?>): OcrMetadataConflict? {
    val distinct = values.filterNotNull().toSet()
    return if (distinct.size > 1) OcrMetadataConflict(field, distinct) else null
  }
  return listOfNotNull(
    conflict("exam", metadata.map { it.examName }),
    conflict("year", metadata.map { it.year?.toString() }),
    conflict("date", metadata.map { it.examDate }),
    conflict("shift", metadata.map { it.shift }),
  )
}

fun OcrSourceMetadata.mergeWith(other: OcrSourceMetadata): OcrSourceMetadata = OcrSourceMetadata(
  examName = examName ?: other.examName,
  year = year ?: other.year,
  examDate = examDate ?: other.examDate,
  shift = shift ?: other.shift,
  confidence = maxOf(confidence, other.confidence),
)

fun extractMetadataAcrossPages(pages: List<OcrPageText>): OcrSourceMetadata = pages
  .asSequence()
  .filter { it.error == null }
  .map { OcrSourceMetadataExtractor.extract(it.text) }
  .fold(OcrSourceMetadata()) { accumulated, next -> accumulated.mergeWith(next) }

fun OcrSourceMetadata.displayParts(): List<String> = buildList {
  examName?.let(::add)
  year?.let { if (examDate == null) add(it.toString()) }
  examDate?.let(::add)
  shift?.let(::add)
}

fun OcrSourceMetadata.reviewLabel(): String = displayParts().joinToString(" • ")
  .ifBlank { "Exam metadata not detected" }

fun OcrSourceMetadata.needsMetadataReview(): Boolean =
  confidence == OcrMetadataConfidence.LOW || confidence == OcrMetadataConfidence.NONE

fun OcrSourceMetadata.reviewGuidance(): OcrMetadataReview = when (confidence) {
  OcrMetadataConfidence.HIGH -> OcrMetadataReview(
    OcrMetadataConfidence.HIGH,
    "Exam metadata was detected with high confidence. Please verify it before saving.",
  )
  OcrMetadataConfidence.MEDIUM -> OcrMetadataReview(
    OcrMetadataConfidence.MEDIUM,
    "Most source metadata was detected. Verify or complete it during review.",
  )
  OcrMetadataConfidence.LOW -> OcrMetadataReview(
    OcrMetadataConfidence.LOW,
    "Only limited source metadata was detected. Complete and verify it during review.",
  )
  OcrMetadataConfidence.NONE -> {
    val hasMetadata = examName != null || year != null || examDate != null || shift != null
    OcrMetadataReview(
      OcrMetadataConfidence.NONE,
      if (hasMetadata) {
        "Source metadata is present without a confidence rating. Verify it before saving."
      } else {
        "No source metadata was detected. Add it manually when known."
      },
    )
  }
}

fun OcrSourceMetadata.metadataNotices(): List<OcrMetadataReviewNotice> = buildList {
  if (examName == null) {
    add(OcrMetadataReviewNotice(
      "Exam name was not detected. Verify the source before saving if exam attribution matters.",
    ))
  }
  if (confidence == OcrMetadataConfidence.LOW) {
    add(OcrMetadataReviewNotice("Only limited exam metadata was detected; verify it against the source."))
  }
}

fun auditOcrMetadata(pages: List<OcrPageText>): OcrMetadataAudit {
  val metadata = extractMetadataAcrossPages(pages)
  return OcrMetadataAudit(metadata, detectMetadataConflicts(pages), metadata.metadataNotices())
}

fun OcrMetadataAudit.summary(): String = when {
  conflicts.isNotEmpty() -> "Conflicting exam metadata detected across OCR pages; verify the source."
  metadata.confidence == OcrMetadataConfidence.HIGH -> metadata.reviewLabel()
  metadata.confidence == OcrMetadataConfidence.MEDIUM -> "${metadata.reviewLabel()} • verify metadata"
  else -> "${metadata.reviewLabel()} • manual source verification recommended"
}
