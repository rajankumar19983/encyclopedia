package com.rajankumar.encyclopaedia.feature.notebook

private val invalidExportNameChars = Regex("[^A-Za-z0-9._-]+")

fun notebookExportName(title: String): String {
  val base = normalizeNotebookTitle(title).ifBlank { "notebook" }
  return base.replace(invalidExportNameChars, "-").trim('-', '.', '_').ifBlank { "notebook" }
}
