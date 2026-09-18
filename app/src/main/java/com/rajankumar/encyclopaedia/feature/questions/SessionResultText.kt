package com.rajankumar.encyclopaedia.feature.questions

fun PracticeSessionSummary.resultHeadline(): String = when (accuracyPercent) {
  100 -> "Perfect session"
  in 80..99 -> "Strong session"
  in 60..79 -> "Good practice"
  else -> "Review and retry"
}

fun PracticeSessionSummary.resultDetail(): String =
  "$correct correct • $incorrect incorrect • $accuracyPercent% accuracy • ${formatPracticeDuration(totalTimeMs)}"
