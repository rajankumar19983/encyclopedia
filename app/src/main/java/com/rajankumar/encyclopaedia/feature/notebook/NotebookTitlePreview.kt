package com.rajankumar.encyclopaedia.feature.notebook

const val NOTEBOOK_TITLE_PREVIEW_LENGTH = 32

fun notebookTitlePreview(title: String): String {
  val value = normalizeNotebookTitle(title).ifBlank { "Untitled page" }
  return if (value.length <= NOTEBOOK_TITLE_PREVIEW_LENGTH) value else value.take(NOTEBOOK_TITLE_PREVIEW_LENGTH - 1).trimEnd() + "…"
}
