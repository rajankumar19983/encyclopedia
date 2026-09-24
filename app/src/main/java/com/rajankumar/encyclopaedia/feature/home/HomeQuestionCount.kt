package com.rajankumar.encyclopaedia.feature.home

fun homeQuestionCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "Question bank empty"
  1 -> "1 question available"
  else -> "$safe questions available"
}
