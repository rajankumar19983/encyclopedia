package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportNotice(val message: String)

fun OcrImportSession.notices(): List<OcrImportNotice> = buildList {
  if (removedDevanagari) add(OcrImportNotice("Hindi/Devanagari OCR text was removed before question parsing."))
  examName?.let { add(OcrImportNotice("Detected exam: $it")) }
  if (summary.needsAttention > 0) add(OcrImportNotice("${summary.needsAttention} draft${if (summary.needsAttention == 1) "" else "s"} require editing before approval."))
}
