package com.rajankumar.encyclopaedia.feature.notebook

fun notebookDeleteMessage(title: String): String {
  val name = normalizeNotebookTitle(title).ifBlank { "this page" }
  return "Delete $name? This cannot be undone."
}
