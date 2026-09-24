package com.rajankumar.encyclopaedia.feature.home

fun homeNotebookCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "No notebook pages"
  1 -> "1 notebook page"
  else -> "$safe notebook pages"
}
