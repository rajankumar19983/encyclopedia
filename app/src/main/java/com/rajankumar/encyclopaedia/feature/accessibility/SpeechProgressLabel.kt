package com.rajankumar.encyclopaedia.feature.accessibility

fun speechSectionProgressLabel(index: Int, total: Int): String {
  if (total <= 0) return "Not reading"
  val safeTotal = total.coerceAtLeast(1)
  val current = (index + 1).coerceIn(1, safeTotal)
  return "Reading section $current of $safeTotal"
}
