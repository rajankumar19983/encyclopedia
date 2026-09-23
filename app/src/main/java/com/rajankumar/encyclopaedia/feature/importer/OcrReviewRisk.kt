package com.rajankumar.encyclopaedia.feature.importer

enum class OcrReviewRisk { LOW, REVIEW_REQUIRED }

fun OcrDraftAudit.reviewRisk(): OcrReviewRisk = if (
  warnings.isNotEmpty() || optionCountQuality != OcrOptionCountQuality.NORMAL || answerConflict.hasConflict
) OcrReviewRisk.REVIEW_REQUIRED else OcrReviewRisk.LOW
