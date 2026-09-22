package com.rajankumar.encyclopaedia.feature.questions

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

data class QuestionQuality(val usable: Boolean, val issues: List<String>)

fun QuestionEntity.assessQuality(): QuestionQuality {
  val options = optionList()
  val issues = buildList {
    if (questionText.trim().length < 5) add("Question text is unusually short")
    if (options.size < PracticeConstants.minOptions) add("Expected at least 2 options")
    if (options.distinctBy { it.lowercase() }.size != options.size) add("Duplicate options detected")
    if (correctOptionIndex() == null) add("Correct answer is invalid")
  }
  return QuestionQuality(issues.isEmpty(), issues)
}
