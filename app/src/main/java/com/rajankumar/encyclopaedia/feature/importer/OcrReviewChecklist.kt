package com.rajankumar.encyclopaedia.feature.importer

data class OcrReviewChecklistItem(val label: String, val required: Boolean = true)

fun ocrReviewChecklist(): List<OcrReviewChecklistItem> = listOf(
  OcrReviewChecklistItem("Question text matches the printed source"),
  OcrReviewChecklistItem("All options are present and in the correct order"),
  OcrReviewChecklistItem("Correct answer was verified"),
  OcrReviewChecklistItem("Exam/source metadata was verified", required = false),
  OcrReviewChecklistItem("No unwanted Hindi/handwritten content remains"),
)
