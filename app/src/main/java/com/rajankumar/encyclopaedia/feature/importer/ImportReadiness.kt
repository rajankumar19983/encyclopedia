package com.rajankumar.encyclopaedia.feature.importer

enum class ImportReadiness { READY_FOR_REVIEW, NEEDS_ATTENTION }

fun ParsedQuestionDraft.importReadiness(): ImportReadiness =
  if (importPolicy().canConfirm) ImportReadiness.READY_FOR_REVIEW else ImportReadiness.NEEDS_ATTENTION
