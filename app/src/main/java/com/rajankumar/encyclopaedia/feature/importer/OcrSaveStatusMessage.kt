package com.rajankumar.encyclopaedia.feature.importer

fun OcrDraftSaveStatus.label(): String = when (this) {
  OcrDraftSaveStatus.READY -> "Approve & Save"
  OcrDraftSaveStatus.SAVING -> "Checking…"
  OcrDraftSaveStatus.SAVED -> "Saved"
  OcrDraftSaveStatus.DUPLICATE -> "Already exists"
  OcrDraftSaveStatus.FAILED -> "Retry Save"
}
