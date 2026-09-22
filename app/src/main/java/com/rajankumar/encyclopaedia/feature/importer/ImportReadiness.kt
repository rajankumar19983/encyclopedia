package com.rajankumar.encyclopaedia.feature.importer

enum class ImportReadiness { READY_FOR_REVIEW, NEEDS_ATTENTION }

fun ParsedQuestionDraft.importReadiness(): ImportReadiness {
  if (questionText.isBlank() || options.size < 2) return ImportReadiness.NEEDS_ATTENTION
  if (correctAnswer == null) return ImportReadiness.NEEDS_ATTENTION
  if (warnings.isNotEmpty()) return ImportReadiness.NEEDS_ATTENTION
  return ImportReadiness.READY_FOR_REVIEW
}
