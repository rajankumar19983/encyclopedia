package com.rajankumar.encyclopaedia.feature.notebook

enum class NotebookSort { RECENT, OLDEST, TITLE }

fun NotebookSort.label(): String = when (this) {
  NotebookSort.RECENT -> "Recently updated"
  NotebookSort.OLDEST -> "Oldest updated"
  NotebookSort.TITLE -> "Title A–Z"
}
