package com.rajankumar.encyclopaedia.feature.importer

data class OcrDraftReviewState(
  val decision: OcrReviewDecision = OcrReviewDecision.PENDING,
  val checklist: OcrReviewChecklistState = OcrReviewChecklistState(),
  val saveStatus: OcrDraftSaveStatus = OcrDraftSaveStatus.READY,
)

enum class OcrDraftSaveStatus { READY, SAVING, SAVED, DUPLICATE, FAILED }
