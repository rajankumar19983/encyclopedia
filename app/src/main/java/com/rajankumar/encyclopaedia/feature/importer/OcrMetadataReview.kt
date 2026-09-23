package com.rajankumar.encyclopaedia.feature.importer

data class OcrMetadataReview(val confidence: OcrMetadataConfidence, val message: String)

fun OcrSourceMetadata.reviewGuidance(): OcrMetadataReview = when (confidence()) {
  OcrMetadataConfidence.COMPLETE -> OcrMetadataReview(OcrMetadataConfidence.COMPLETE, "Exam and year metadata were detected. Verify them before saving.")
  OcrMetadataConfidence.PARTIAL -> OcrMetadataReview(OcrMetadataConfidence.PARTIAL, "Only part of the source metadata was detected. Complete or verify it during review.")
  OcrMetadataConfidence.NONE -> OcrMetadataReview(OcrMetadataConfidence.NONE, "No exam/year metadata was detected. Add it manually when known.")
}
