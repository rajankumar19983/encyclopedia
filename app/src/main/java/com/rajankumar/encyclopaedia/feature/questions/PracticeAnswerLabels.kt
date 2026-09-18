package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun QuestionEntity.answerLabel(letter: String): String {
  val index = letter.trim().uppercase().singleOrNull()?.code?.minus('A'.code) ?: return letter
  val option = optionList().getOrNull(index) ?: return letter
  return "${optionLetter(index)}. $option"
}
