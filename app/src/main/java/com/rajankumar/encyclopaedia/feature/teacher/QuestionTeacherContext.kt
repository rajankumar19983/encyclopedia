package com.rajankumar.encyclopaedia.feature.teacher

import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun QuestionEntity.asTeacherContext(
  options: List<String>,
  answerRevealed: Boolean = false
): TeacherContext {
  val supporting = buildList {
    options.forEachIndexed { index, option ->
      add("${('A'.code + index).toChar()}. $option")
    }
    if (answerRevealed) {
      add("Correct answer: ${correctAnswer.trim().uppercase()}")
      explanation?.trim()?.takeIf(String::isNotEmpty)?.let {
        add("Explanation: $it")
      }
    }
  }

  return TeacherContext(
    screen = "Practice",
    title = "Multiple-choice question",
    primaryContent = questionText,
    supportingContent = supporting
  )
}
