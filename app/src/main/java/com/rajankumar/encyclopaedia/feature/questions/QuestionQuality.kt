package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class QuestionQuality(val usable: Boolean, val issues: List<String>)

fun QuestionEntity.assessQuality(): QuestionQuality {
  val issues = buildList {
    if (questionText.trim().length < 5) add("Question text is unusually short")
    if (optionList().size !in PracticeConstants.minOptions..PracticeConstants.maxOptions) add("Expected 2–6 options")
    if (optionList().distinctBy { it.lowercase() }.size != optionList().size) add("Duplicate options detected")
    if (correctOptionIndex() == null) add("Correct answer is invalid")
  }
  return QuestionQuality(issues.isEmpty(), issues)
}
