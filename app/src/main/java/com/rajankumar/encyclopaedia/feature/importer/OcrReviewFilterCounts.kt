package com.rajankumar.encyclopaedia.feature.importer

fun OcrReviewFilter.count(decisions: List<OcrReviewDecision>): Int = decisions.count(::matches)
