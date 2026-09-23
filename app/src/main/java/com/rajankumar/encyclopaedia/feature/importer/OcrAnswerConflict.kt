package com.rajankumar.encyclopaedia.feature.importer

private val explicitAnswerToken = Regex(
  "^\\s*(?:ans(?:wer)?|correct\\s*(?:answer|option))\\s*[:.\\-]?\\s*[(]?([A-Za-z]|\\d{1,2})[)]?.*$",
  RegexOption.IGNORE_CASE,
)

data class OcrAnswerConflict(val answers: List<String>) {
  val hasConflict: Boolean get() = answers.distinct().size > 1
}

fun detectOcrAnswerConflict(lines: List<String>): OcrAnswerConflict = OcrAnswerConflict(
  lines.mapNotNull { explicitAnswerToken.matchEntire(it)?.groupValues?.get(1)?.let(::normalizeOcrAnswerToken) },
)
