package com.rajankumar.encyclopaedia.feature.home

fun homeRevisionCountLabel(count: Int): String = when (val safe = count.coerceAtLeast(0)) {
  0 -> "Revision queue clear"
  1 -> "1 question to revise"
  else -> "$safe questions to revise"
}
