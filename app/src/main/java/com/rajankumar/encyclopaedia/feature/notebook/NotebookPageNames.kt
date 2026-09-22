package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity

internal fun normalizedPageName(value: String): String =
  value.trim().replace(Regex("\\s+"), " ")

internal fun isPageNameAvailable(
  pages: List<NotebookPageEntity>,
  candidate: String,
  excludingPageId: String? = null
): Boolean {
  val normalized = normalizedPageName(candidate)
  if (normalized.isBlank()) return false
  return pages.none {
    it.id != excludingPageId &&
      normalizedPageName(it.title).equals(normalized, ignoreCase = true)
  }
}
