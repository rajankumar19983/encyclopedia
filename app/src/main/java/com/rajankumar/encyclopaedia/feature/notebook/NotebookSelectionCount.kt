package com.rajankumar.encyclopaedia.feature.notebook

fun notebookSelectionCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "Nothing selected"
  1 -> "1 stroke selected"
  else -> "$safe strokes selected"
}
