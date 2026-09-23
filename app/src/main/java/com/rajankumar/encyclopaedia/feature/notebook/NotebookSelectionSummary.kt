package com.rajankumar.encyclopaedia.feature.notebook

internal fun notebookSelectionSummary(selectedCount: Int): String = when (selectedCount) {
  0 -> "No strokes selected"
  1 -> "1 stroke selected"
  else -> "$selectedCount strokes selected"
}
