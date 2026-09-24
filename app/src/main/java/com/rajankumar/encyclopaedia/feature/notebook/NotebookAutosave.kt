package com.rajankumar.encyclopaedia.feature.notebook

enum class NotebookAutosaveStatus { SAVED, SAVING, UNSAVED }

fun NotebookAutosaveStatus.label(): String = when (this) {
  NotebookAutosaveStatus.SAVED -> "Saved"
  NotebookAutosaveStatus.SAVING -> "Saving…"
  NotebookAutosaveStatus.UNSAVED -> "Unsaved changes"
}
