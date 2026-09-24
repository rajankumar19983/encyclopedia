package com.rajankumar.encyclopaedia.feature.home

fun homeDashboardAccessibilitySummary(topics: Int, questions: Int, streakDays: Int, progressPercent: Int): String =
  "Dashboard: ${topics.coerceAtLeast(0)} topics, ${questions.coerceAtLeast(0)} questions, ${streakDays.coerceAtLeast(0)} day streak, ${progressPercent.coerceIn(0, 100)} percent overall progress."
