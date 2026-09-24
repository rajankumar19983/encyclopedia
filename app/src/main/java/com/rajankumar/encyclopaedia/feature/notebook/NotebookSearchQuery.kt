package com.rajankumar.encyclopaedia.feature.notebook

fun normalizeNotebookSearchQuery(query: String): String = query.trim().replace(Regex("\\s+"), " ").take(100)
fun hasNotebookSearchQuery(query: String): Boolean = normalizeNotebookSearchQuery(query).isNotEmpty()
