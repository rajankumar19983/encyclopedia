package com.rajankumar.encyclopaedia.feature.notebook

fun notebookDuplicateTitle(title: String): String {
  val normalized = normalizeNotebookTitle(title).ifBlank { "Untitled page" }
  return "$normalized copy"
}
