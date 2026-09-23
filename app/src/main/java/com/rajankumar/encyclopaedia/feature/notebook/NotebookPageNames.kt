package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookPageEntity

internal const val NOTEBOOK_PAGE_TITLE_MAX_LENGTH = 100

internal fun normalizedPageName(value: String): String =
  value.trim().replace(Regex("\\s+"), " ")

internal fun isPageNameAvailable(
  pages: List<NotebookPageEntity>,
  candidate: String,
  excludingPageId: String? = null
): Boolean {
  val normalized = normalizedPageName(candidate)
  if (normalized.isBlank() || normalized.length > NOTEBOOK_PAGE_TITLE_MAX_LENGTH) return false
  return pages.none {
    it.id != excludingPageId &&
      normalizedPageName(it.title).equals(normalized, ignoreCase = true)
  }
}
