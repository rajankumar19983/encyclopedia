package com.rajankumar.encyclopaedia.feature.importer

data class OcrDraftEditState(val question: String, val optionsText: String, val answer: String) {
  fun editable(): EditableImportDraft = EditableImportDraft(question, optionsText.lines(), answer)
  fun validation(): EditableImportValidation = editable().validateForSave()
}

fun ParsedQuestionDraft.toEditState(): OcrDraftEditState = OcrDraftEditState(questionText, options.joinToString("\n"), correctAnswer.orEmpty())
