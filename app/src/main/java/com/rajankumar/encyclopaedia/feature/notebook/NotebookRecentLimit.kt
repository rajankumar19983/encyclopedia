package com.rajankumar.encyclopaedia.feature.notebook

const val DEFAULT_RECENT_NOTEBOOK_LIMIT = 6

fun notebookRecentLimit(requested: Int): Int = requested.coerceIn(1, 20)
