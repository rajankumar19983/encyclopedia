package com.rajankumar.encyclopaedia.feature.questions

fun practiceCompletion(answered: Int, total: Int): Int =
  if (total <= 0) 0 else ((answered.coerceIn(0, total) * 100f) / total).toInt()
