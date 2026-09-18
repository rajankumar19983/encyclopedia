package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun QuestionEntity.optionList(): List<String> = options
  .lines()
  .map(String::trim)
  .filter(String::isNotBlank)
  .take(6)

fun optionLetter(index: Int): String = ('A'.code + index).toChar().toString()

fun QuestionEntity.correctOptionIndex(): Int? {
  val letter = correctAnswer.trim().uppercase().singleOrNull() ?: return null
  val index = letter.code - 'A'.code
  return index.takeIf { it in optionList().indices }
}
