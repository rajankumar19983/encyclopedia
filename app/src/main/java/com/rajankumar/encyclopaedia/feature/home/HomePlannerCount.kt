package com.rajankumar.encyclopaedia.feature.home

fun homePlannerCountLabel(completed: Int, total: Int): String {
  val safeTotal = total.coerceAtLeast(0)
  val safeCompleted = completed.coerceIn(0, safeTotal)
  return if (safeTotal == 0) "No tasks planned" else "$safeCompleted of $safeTotal tasks complete"
}
