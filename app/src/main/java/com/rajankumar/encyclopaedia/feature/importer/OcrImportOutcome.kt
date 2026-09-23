package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportOutcome(val approved: Int, val rejected: Int, val duplicates: Int) {
  val saved: Int get() = (approved - duplicates).coerceAtLeast(0)
}

fun OcrImportOutcome.message(): String = "$saved saved • $rejected rejected • $duplicates skipped as duplicate${if (duplicates == 1) "" else "s"}."
