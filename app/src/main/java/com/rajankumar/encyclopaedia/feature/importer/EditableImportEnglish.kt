package com.rajankumar.encyclopaedia.feature.importer

fun EditableImportDraft.englishOnly(): EditableImportDraft = copy(
  question = OcrEnglishTextFilter.filter(question),
  options = options.map(OcrEnglishTextFilter::filter),
  answer = answer.trim()
)
