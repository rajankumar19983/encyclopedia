package com.rajankumar.encyclopaedia.feature.home

data class HomePlanRecommendation(val title: String, val detail: String)

fun recommendHomePlan(questionCount: Int, attemptCount: Int, coveragePercent: Int, accuracyPercent: Int): HomePlanRecommendation = when {
  questionCount == 0 -> HomePlanRecommendation("Build your question bank", "Import or create questions before starting practice.")
  attemptCount == 0 -> HomePlanRecommendation("Start a baseline session", "Answer a small mixed set so the app can learn your weak areas.")
  accuracyPercent < 60 -> HomePlanRecommendation("Review mistakes first", "Revisit incorrect questions before adding more new material.")
  coveragePercent < 100 -> HomePlanRecommendation("Expand your coverage", "Practice unseen questions, then finish with mistake review.")
  else -> HomePlanRecommendation("Strengthen recall", "Mix random practice with revision of previous mistakes.")
}
