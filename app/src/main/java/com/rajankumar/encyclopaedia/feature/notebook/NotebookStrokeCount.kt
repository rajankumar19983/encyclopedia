package com.rajankumar.encyclopaedia.feature.notebook

fun notebookStrokeCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "Blank page"
  1 -> "1 stroke"
  else -> "$safe strokes"
}
