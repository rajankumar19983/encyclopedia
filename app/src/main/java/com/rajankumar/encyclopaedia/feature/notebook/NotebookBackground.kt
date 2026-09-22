package com.rajankumar.encyclopaedia.feature.notebook

internal fun parseNotebookBackground(value: String): NotebookBackground? =
  NotebookBackground.entries.firstOrNull { it.name == value.trim().uppercase() }

internal fun isSupportedNotebookBackground(value: String): Boolean =
  parseNotebookBackground(value) != null
