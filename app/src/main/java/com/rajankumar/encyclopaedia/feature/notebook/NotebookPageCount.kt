package com.rajankumar.encyclopaedia.feature.notebook

fun notebookPageCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "No pages"
  1 -> "1 page"
  else -> "$safe pages"
}
