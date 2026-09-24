package com.rajankumar.encyclopaedia.feature.notebook

fun notebookPageNumberLabel(index: Int, total: Int): String {
  val safeTotal = total.coerceAtLeast(1)
  val page = (index + 1).coerceIn(1, safeTotal)
  return "Page $page of $safeTotal"
}
