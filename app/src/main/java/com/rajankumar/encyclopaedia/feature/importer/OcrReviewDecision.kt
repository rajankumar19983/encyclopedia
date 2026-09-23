package com.rajankumar.encyclopaedia.feature.importer

enum class OcrReviewDecision { PENDING, APPROVED, REJECTED }

fun OcrReviewDecision.isResolved(): Boolean = this != OcrReviewDecision.PENDING
