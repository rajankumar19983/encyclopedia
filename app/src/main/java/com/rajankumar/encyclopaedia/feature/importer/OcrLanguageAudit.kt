package com.rajankumar.encyclopaedia.feature.importer

data class OcrLanguageAudit(
  val questionHasDevanagari: Boolean,
  val optionIndexesWithDevanagari: List<Int>,
  val explanationHasDevanagari: Boolean
) {
  val clean: Boolean get() = !questionHasDevanagari && optionIndexesWithDevanagari.isEmpty() && !explanationHasDevanagari
}

fun ParsedQuestionDraft.languageAudit(): OcrLanguageAudit = OcrLanguageAudit(
  questionHasDevanagari = OcrEnglishTextFilter.containsDevanagari(questionText),
  optionIndexesWithDevanagari = options.mapIndexedNotNull { index, option -> index.takeIf { OcrEnglishTextFilter.containsDevanagari(option) } },
  explanationHasDevanagari = explanation?.let(OcrEnglishTextFilter::containsDevanagari) == true
)
