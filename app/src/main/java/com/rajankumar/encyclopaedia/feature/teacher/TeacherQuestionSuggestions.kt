package com.rajankumar.encyclopaedia.feature.teacher

fun teacherQuestionSuggestions(context: TeacherContext?): List<String> = when (context?.screen?.lowercase()) {
  "question", "practice", "test" -> listOf(
    "Explain this question without giving away the answer.",
    "What concept is this question testing?",
    "What common exam trap should I watch for?",
  )
  "lesson", "note", "topic" -> listOf(
    "Explain this in simpler words.",
    "Give me an exam-focused summary.",
    "Quiz me on what is visible here.",
  )
  else -> listOf(
    "Explain the current screen.",
    "What should I revise next?",
    "Ask me a quick concept-check question.",
  )
}
