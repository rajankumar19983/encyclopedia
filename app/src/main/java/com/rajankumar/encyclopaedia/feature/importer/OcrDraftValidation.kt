package com.rajankumar.encyclopaedia.feature.importer

data class OcrDraftValidation(val warnings: List<String>) {
  val clean: Boolean get() = warnings.isEmpty()
}

fun validateOcrDraft(draft: ParsedQuestionDraft): OcrDraftValidation {
  val warnings = buildList {
    addAll(draft.warnings)
    detectDuplicateOcrOptions(draft.options).warning()?.let(::add)
    val answer = checkOcrAnswerRange(draft.correctAnswer, draft.options.size)
    if (!answer.valid) add("Correct answer does not map to one of the extracted options.")
    when (classifyOcrQuestionLength(draft.questionText)) {
      OcrQuestionLengthQuality.TOO_SHORT -> add("Question text is unusually short; verify OCR.")
      OcrQuestionLengthQuality.VERY_LONG -> add("Question text is unusually long; verify question boundaries.")
      OcrQuestionLengthQuality.NORMAL -> Unit
    }
    if (classifyOcrOptionCount(draft.options.size) == OcrOptionCountQuality.UNUSUALLY_MANY) add("Unusually many options were extracted; verify question boundaries.")
  }
  return OcrDraftValidation(warnings.distinct())
}
