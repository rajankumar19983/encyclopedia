package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportReviewBundle(
  val session: OcrImportSession,
  val queue: ImportReviewQueue
)

fun prepareOcrImportReview(rawText: String): OcrImportReviewBundle {
  val session = prepareOcrImportSession(rawText)
  return OcrImportReviewBundle(session, ImportReviewQueue(session.toEditableReviewItems()))
}
