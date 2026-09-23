package com.rajankumar.encyclopaedia.feature.importer

data class OcrQuestionSequenceCheck(val numbers: List<Int>, val missing: List<Int>, val duplicates: List<Int>) {
  val needsReview: Boolean get() = missing.isNotEmpty() || duplicates.isNotEmpty()
}

fun checkQuestionSequence(numbers: List<Int>): OcrQuestionSequenceCheck {
  if (numbers.isEmpty()) return OcrQuestionSequenceCheck(emptyList(), emptyList(), emptyList())
  val duplicates = numbers.groupingBy { it }.eachCount().filterValues { it > 1 }.keys.sorted()
  val unique = numbers.distinct().sorted()
  val missing = if (unique.size < 2) emptyList() else (unique.first()..unique.last()).filterNot(unique::contains)
  return OcrQuestionSequenceCheck(numbers, missing, duplicates)
}
