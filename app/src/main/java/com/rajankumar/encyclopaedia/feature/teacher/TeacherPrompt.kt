package com.rajankumar.encyclopaedia.feature.teacher

fun buildTeacherPrompt(
  question: String,
  context: TeacherContext?
): String {
  val normalizedQuestion = question.trim()
  require(normalizedQuestion.isNotEmpty()) { "Teacher question cannot be blank" }

  return buildString {
    appendLine("You are the in-app AI teacher for a competitive-exam study application.")
    appendLine("Explain accurately and teach step by step. Focus on conceptual depth, exam traps, and why alternatives are wrong when relevant.")
    appendLine("Do not invent information that is not present in the supplied screen context. If more information is required, say so clearly.")
    if (context != null) {
      appendLine()
      appendLine("VISIBLE SCREEN CONTEXT")
      appendLine(context.asPromptContext())
    }
    appendLine()
    appendLine("STUDENT QUESTION")
    append(normalizedQuestion)
  }
}
