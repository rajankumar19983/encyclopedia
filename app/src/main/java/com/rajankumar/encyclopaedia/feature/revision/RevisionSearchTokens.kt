package com.rajankumar.encyclopaedia.feature.revision

fun revisionSearchTokens(query: String): List<String> = normalizeRevisionQuery(query)
  .split(' ')
  .map { it.trim().lowercase() }
  .filter { it.length >= 2 }
  .distinct()
