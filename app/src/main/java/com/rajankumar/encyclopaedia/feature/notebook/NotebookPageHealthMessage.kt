package com.rajankumar.encyclopaedia.feature.notebook

fun NotebookPageHealth.message(): String = when (this) {
  NotebookPageHealth.BLANK -> "Start writing or drawing on this page."
  NotebookPageHealth.LIGHT -> "This page has plenty of room for more notes."
  NotebookPageHealth.DETAILED -> "This page contains substantial handwritten content."
  NotebookPageHealth.VERY_DETAILED -> "Consider continuing on a new page to keep drawing responsive."
}
