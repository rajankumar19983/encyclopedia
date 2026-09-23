package com.rajankumar.encyclopaedia.feature.notebook

internal const val NOTEBOOK_MIN_PAGE_WIDTH = 320f
internal const val NOTEBOOK_MIN_PAGE_HEIGHT = 480f
internal const val NOTEBOOK_MAX_PAGE_WIDTH = 8000f
internal const val NOTEBOOK_MAX_PAGE_HEIGHT = 12000f

internal fun isValidNotebookPageSize(width: Float, height: Float): Boolean =
  width.isFinite() && height.isFinite() &&
    width in NOTEBOOK_MIN_PAGE_WIDTH..NOTEBOOK_MAX_PAGE_WIDTH &&
    height in NOTEBOOK_MIN_PAGE_HEIGHT..NOTEBOOK_MAX_PAGE_HEIGHT
