package com.rajankumar.encyclopaedia.feature.revision

fun List<RevisionItem>.revisionRecommendation(): String {
  val stats = revisionStats()
  return when {
    stats.total == 0 -> "No revision queue yet. Keep practising."
    stats.urgent > 0 -> "Start with ${stats.urgent} urgent question${if (stats.urgent == 1) "" else "s"}."
    stats.repeatedMistakes > 0 -> "Focus on repeated mistakes before moving to new material."
    else -> "A short revision session will reinforce recent mistakes."
  }
}
