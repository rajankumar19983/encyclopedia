package com.rajankumar.encyclopaedia.feature.importer

data class OcrImportPreparation(
  val sanitized: OcrSanitizationResult,
  val drafts: List<ParsedQuestionDraft>,
  val metadata: OcrSourceMetadata,
  val diagnostics: OcrBatchDiagnostics,
)

fun prepareOcrImport(rawText: String): OcrImportPreparation {
  val sanitized = sanitizeOcrText(rawText)
  val drafts = OcrQuestionParser.parse(sanitized.text).map { it.withQualityWarnings() }
  return OcrImportPreparation(
    sanitized = sanitized,
    drafts = drafts,
    metadata = OcrSourceMetadataExtractor.extract(rawText),
    diagnostics = buildOcrBatchDiagnostics(sanitized.text, drafts.size),
  )
}
