package com.rajankumar.encyclopaedia.feature.notebook

enum class NotebookSaveState { SAVED, DIRTY, SAVING, FAILED }

internal fun NotebookSaveState.label(): String = when (this) {
  NotebookSaveState.SAVED -> "Saved"
  NotebookSaveState.DIRTY -> "Unsaved changes"
  NotebookSaveState.SAVING -> "Saving…"
  NotebookSaveState.FAILED -> "Save failed"
}
