package com.rajankumar.encyclopaedia.feature.importer

fun normalizeOcrAnswerToken(value: String): String? {
  val token = value.trim().removePrefix("(").removeSuffix(")").trim()
  val number = token.toIntOrNull()
  if (number != null) return if (number in 1..26) ('A'.code + number - 1).toChar().toString() else null
  val letter = token.uppercase().singleOrNull()
  return letter?.takeIf { it in 'A'..'Z' }?.toString()
}
