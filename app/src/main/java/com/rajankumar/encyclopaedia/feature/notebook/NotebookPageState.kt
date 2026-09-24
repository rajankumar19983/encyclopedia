package com.rajankumar.encyclopaedia.feature.notebook

data class NotebookPageState(val title: String, val strokeCount: Int, val autosaveStatus: NotebookAutosaveStatus) {
  val summary: String get() = "${normalizeNotebookTitle(title).ifBlank { "Untitled page" }} • ${notebookStrokeCountLabel(strokeCount)} • ${autosaveStatus.label()}"
}
