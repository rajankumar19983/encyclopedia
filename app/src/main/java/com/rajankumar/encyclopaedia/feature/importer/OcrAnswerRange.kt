package com.rajankumar.encyclopaedia.feature.importer

data class OcrAnswerRangeCheck(val normalizedAnswer: String?, val valid: Boolean)

fun checkOcrAnswerRange(answer: String?, optionCount: Int): OcrAnswerRangeCheck {
  val normalized = answer?.let(::normalizeOcrAnswerToken)
  if (normalized == null || optionCount <= 0) return OcrAnswerRangeCheck(normalized, false)
  val index = normalized.single() - 'A'
  return OcrAnswerRangeCheck(normalized, index in 0 until optionCount)
}
