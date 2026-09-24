package com.rajankumar.encyclopaedia.feature.notebook

const val NOTEBOOK_MAX_TITLE_LENGTH = 100

fun normalizeNotebookTitle(value: String): String = value.trim().replace(Regex("\\s+"), " ").take(NOTEBOOK_MAX_TITLE_LENGTH)
fun isValidNotebookTitle(value: String): Boolean = normalizeNotebookTitle(value).isNotEmpty()
