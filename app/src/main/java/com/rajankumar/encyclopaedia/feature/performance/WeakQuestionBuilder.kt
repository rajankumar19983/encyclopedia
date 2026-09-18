package com.rajankumar.encyclopaedia.feature.performance

import com.rajankumar.encyclopaedia.data.local.QuestionAttemptEntity
import com.rajankumar.encyclopaedia.data.local.QuestionEntity

fun buildWeakQuestions(questions: List<QuestionEntity>, attempts: List<QuestionAttemptEntity>): List<WeakQuestion> {
  val byQuestion = attempts.groupBy { it.questionId }
  return questions.mapNotNull { question ->
    val history = byQuestion[question.id].orEmpty()
    val mistakes = history.count { !it.isCorrect }
    if (mistakes == 0) null else WeakQuestion(question, history.size, mistakes)
  }.sortedWith(compareByDescending<WeakQuestion> { it.mistakes }.thenBy { it.accuracy })
}
