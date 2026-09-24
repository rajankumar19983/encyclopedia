package com.rajankumar.encyclopaedia.feature.revision

enum class RevisionMastery(val label: String) { NEEDS_WORK("Needs work"), DEVELOPING("Developing"), STRONG("Strong") }

fun revisionMastery(accuracyPercent: Int): RevisionMastery = when (accuracyPercent.coerceIn(0, 100)) {
  in 0..49 -> RevisionMastery.NEEDS_WORK
  in 50..79 -> RevisionMastery.DEVELOPING
  else -> RevisionMastery.STRONG
}
