package com.rajankumar.encyclopaedia.feature.questions

fun practiceFeedback(correct: Int, total: Int): String {
  if (total == 0) return "Complete a practice session to start building your performance history."
  return when (accuracyPercent(correct, total)) {
    in 90..100 -> "Strong accuracy. Keep revisiting questions so recall stays durable."
    in 75..89 -> "Good progress. Review the questions you missed before the next session."
    in 50..74 -> "Review incorrect answers and their underlying topics before retrying."
    else -> "Spend some time learning the underlying topics, then practise them again."
  }
}
