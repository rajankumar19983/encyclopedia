package com.rajankumar.encyclopaedia.feature.importer

data class OcrMetadataReview(val confidence: OcrMetadataConfidence, val message: String)

fun OcrSourceMetadata.reviewGuidance(): OcrMetadataReview = when (confidence) {
  OcrMetadataConfidence.HIGH -> OcrMetadataReview(
    OcrMetadataConfidence.HIGH,
    "Exam metadata was detected with high confidence. Please verify it before saving."
  )
  OcrMetadataConfidence.MEDIUM -> OcrMetadataReview(
    OcrMetadataConfidence.MEDIUM,
    "Most source metadata was detected. Verify or complete it during review."
  )
  OcrMetadataConfidence.LOW -> OcrMetadataReview(
    OcrMetadataConfidence.LOW,
    "Only limited source metadata was detected. Complete and verify it during review."
  )
  OcrMetadataConfidence.NONE -> {
    val hasMetadata = examName != null || year != null || examDate != null || shift != null
    OcrMetadataReview(
      OcrMetadataConfidence.NONE,
      if (hasMetadata) {
        "Source metadata is present without a confidence rating. Verify it before saving."
      } else {
        "No source metadata was detected. Add it manually when known."
      }
    )
  }
}
