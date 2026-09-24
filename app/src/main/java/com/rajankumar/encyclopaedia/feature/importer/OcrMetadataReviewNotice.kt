package com.rajankumar.encyclopaedia.feature.importer

data class OcrMetadataReviewNotice(val message: String)

fun OcrSourceMetadata.metadataNotices(): List<OcrMetadataReviewNotice> = buildList {
  if (examName == null) add(OcrMetadataReviewNotice("Exam name was not detected. Verify the source before saving if exam attribution matters."))
  if (confidence == OcrMetadataConfidence.LOW) add(OcrMetadataReviewNotice("Only limited exam metadata was detected; verify it against the source."))
}
