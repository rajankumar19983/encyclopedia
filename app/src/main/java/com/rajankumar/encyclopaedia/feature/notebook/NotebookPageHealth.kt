package com.rajankumar.encyclopaedia.feature.notebook

enum class NotebookPageHealth { BLANK, LIGHT, DETAILED, VERY_DETAILED }

fun notebookPageHealth(strokeCount: Int): NotebookPageHealth = when (strokeCount.coerceAtLeast(0)) {
  0 -> NotebookPageHealth.BLANK
  in 1..999 -> NotebookPageHealth.LIGHT
  in 1_000..4_999 -> NotebookPageHealth.DETAILED
  else -> NotebookPageHealth.VERY_DETAILED
}
