package com.rajankumar.encyclopaedia.feature.importer

data class OcrMetadataAudit(
  val metadata: OcrSourceMetadata,
  val conflicts: List<OcrMetadataConflict>,
  val notices: List<OcrMetadataReviewNotice>
) {
  val requiresReview: Boolean get() = conflicts.isNotEmpty() || metadata.needsMetadataReview()
}

fun auditOcrMetadata(pages: List<OcrPageText>): OcrMetadataAudit {
  val metadata = extractMetadataAcrossPages(pages)
  return OcrMetadataAudit(metadata, detectMetadataConflicts(pages), metadata.metadataNotices())
}
