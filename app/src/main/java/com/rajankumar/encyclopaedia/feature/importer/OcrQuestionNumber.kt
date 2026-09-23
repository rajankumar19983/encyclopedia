package com.rajankumar.encyclopaedia.feature.importer

private val numberedQuestion = Regex("^\\s*(?:Q(?:uestion)?\\s*)?(\\d{1,4})\\s*[.)\\-:]", RegexOption.IGNORE_CASE)

fun extractQuestionNumbers(rawText: String): List<Int> = rawText.lineSequence()
  .mapNotNull { line -> numberedQuestion.find(line)?.groupValues?.get(1)?.toIntOrNull() }
  .toList()
