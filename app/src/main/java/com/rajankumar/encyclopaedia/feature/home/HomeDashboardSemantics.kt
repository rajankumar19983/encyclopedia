package com.rajankumar.encyclopaedia.feature.home

fun homeDashboardAccessibilitySummary(topics: Int, questions: Int, streakDays: Int, progressPercent: Int): String {
  val safeTopics = topics.coerceAtLeast(0)
  val safeQuestions = questions.coerceAtLeast(0)
  if (safeTopics == 0 && safeQuestions == 0) return "Dashboard is ready. Add topics or import PYQ questions to begin."
  return "Dashboard: $safeTopics topics, $safeQuestions questions, ${streakDays.coerceAtLeast(0)} day streak, ${progressPercent.coerceIn(0, 100)} percent overall progress."
}
