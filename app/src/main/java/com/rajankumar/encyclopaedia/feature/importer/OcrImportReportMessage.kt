package com.rajankumar.encyclopaedia.feature.importer

fun OcrImportReport.message(): String = buildList {
  add("Prepared $draftCount review ${if (draftCount == 1) "draft" else "drafts"}.")
  if (removedLineCount > 0) add("Excluded $removedLineCount suspect OCR ${if (removedLineCount == 1) "line" else "lines"}.")
  structuralWarning?.let(::add)
}.joinToString(" ")
