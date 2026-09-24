package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportReport(
  val draftCount: Int,
  val removedLineCount: Int,
  val metadataConfidence: OcrMetadataConfidence,
  val structuralWarning: String?,
)

fun OcrImportPreparation.report(): OcrImportReport = OcrImportReport(
  draftCount = drafts.size,
  removedLineCount = sanitized.removedLines.size,
  metadataConfidence = metadata.confidence,
  structuralWarning = diagnostics.message(),
)
