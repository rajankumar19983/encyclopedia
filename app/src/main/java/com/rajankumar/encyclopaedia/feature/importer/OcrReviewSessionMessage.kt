package com.rajankumar.encyclopaedia.feature.importer

fun OcrReviewSession.message(): String = if (complete) {
  "Review complete • $approved approved • $rejected rejected."
} else {
  "$pending of $draftCount drafts still need an explicit decision."
}
