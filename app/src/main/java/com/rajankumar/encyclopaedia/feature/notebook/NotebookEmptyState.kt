package com.rajankumar.encyclopaedia.feature.notebook

data class NotebookEmptyState(val title: String, val message: String)

fun notebookEmptyState(hasSearch: Boolean): NotebookEmptyState = if (hasSearch) {
  NotebookEmptyState("No matching notes", "Try a different search term.")
} else {
  NotebookEmptyState("No notebook pages yet", "Create a page to start handwriting notes.")
}
