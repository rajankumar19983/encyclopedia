package com.rajankumar.encyclopaedia.feature.importer

private val explicitAnswerToken = Regex(
  "^\\s*(?:ans(?:wer)?|correct\\s*(?:answer|option))\\s*[:.\\-]?\\s*[(]?([A-Za-z]|\\d{1,2})[)]?.*$",
  RegexOption.IGNORE_CASE,
)

data class OcrAnswerConflict(val answers: List<String>) {
  val hasConflict: Boolean get() = answers.distinct().size > 1
}

data class OcrAnswerRangeCheck(val normalizedAnswer: String?, val valid: Boolean)

fun normalizeOcrAnswerToken(value: String): String? {
  val token = value.trim().removePrefix("(").removeSuffix(")").trim()
  val number = token.toIntOrNull()
  if (number != null) return if (number in 1..26) ('A'.code + number - 1).toChar().toString() else null
  val letter = token.uppercase().singleOrNull()
  return letter?.takeIf { it in 'A'..'Z' }?.toString()
}

fun detectOcrAnswerConflict(lines: List<String>): OcrAnswerConflict = OcrAnswerConflict(
  lines.mapNotNull { explicitAnswerToken.matchEntire(it)?.groupValues?.get(1)?.let(::normalizeOcrAnswerToken) },
)

fun OcrAnswerConflict.warning(): String? = if (hasConflict) {
  "Conflicting printed answer markers were detected (${answers.distinct().joinToString()}). Verify the correct option manually."
} else {
  null
}

fun checkOcrAnswerRange(answer: String?, optionCount: Int): OcrAnswerRangeCheck {
  val normalized = answer?.let(::normalizeOcrAnswerToken)
  if (normalized == null || optionCount <= 0) return OcrAnswerRangeCheck(normalized, false)
  val index = normalized.single() - 'A'
  return OcrAnswerRangeCheck(normalized, index in 0 until optionCount)
}
