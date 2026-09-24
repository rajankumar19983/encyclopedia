package com.rajankumar.encyclopaedia.feature.notebook

const val NOTEBOOK_AUTOSAVE_DEBOUNCE_MS = 750L

fun notebookAutosaveDelay(hasUnsavedChanges: Boolean): Long = if (hasUnsavedChanges) NOTEBOOK_AUTOSAVE_DEBOUNCE_MS else 0L
