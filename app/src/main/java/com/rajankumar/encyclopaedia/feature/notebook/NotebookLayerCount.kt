package com.rajankumar.encyclopaedia.feature.notebook

fun notebookLayerCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "No layers"
  1 -> "1 layer"
  else -> "$safe layers"
}
