package com.rajankumar.encyclopaedia.feature.teacher

import org.junit.Assert.assertTrue
import org.junit.Test

class TeacherQuestionSuggestionsTest {
  @Test fun questionSuggestionsAvoidAnswerLeakage() {
    val context = TeacherContext("question", primaryContent = "Question text")
    assertTrue(teacherQuestionSuggestions(context).first().contains("without giving away the answer"))
  }

  @Test fun lessonSuggestionsIncludeExamSummary() {
    val context = TeacherContext("lesson", primaryContent = "Lesson text")
    assertTrue(teacherQuestionSuggestions(context).any { it.contains("exam-focused") })
  }
}
