package com.rajankumar.encyclopaedia.feature.importer

fun EditableImportDraft.hasOnlyEnglishOcrContent(): Boolean =
  !OcrEnglishTextFilter.containsDevanagari(question) &&
    options.none(OcrEnglishTextFilter::containsDevanagari)
