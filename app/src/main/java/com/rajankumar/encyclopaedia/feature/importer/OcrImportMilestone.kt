package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportMilestone(
  val imageOcr: Boolean,
  val pdfOcr: Boolean,
  val englishFiltering: Boolean,
  val variableOptions: Boolean,
  val explicitReview: Boolean,
  val duplicateProtection: Boolean,
  val sourceMetadata: Boolean,
  val noMediaRetention: Boolean,
) {
  val complete: Boolean get() = imageOcr && pdfOcr && englishFiltering && variableOptions && explicitReview && duplicateProtection && sourceMetadata && noMediaRetention
}
