package com.rajankumar.encyclopaedia.feature.notebook

import com.rajankumar.encyclopaedia.data.local.NotebookLayerEntity

internal const val NOTEBOOK_LAYER_NAME_MAX_LENGTH = 40
internal const val NOTEBOOK_PAGE_NAME_MAX_LENGTH = 100

internal fun normalizedLayerName(value: String): String =
  value.trim().replace(Regex("\\s+"), " ")

internal fun isLayerNameAvailable(
  layers: List<NotebookLayerEntity>,
  candidate: String,
  excludingLayerId: String? = null
): Boolean {
  val normalized = normalizedLayerName(candidate)
  if (normalized.isBlank() || normalized.length > NOTEBOOK_LAYER_NAME_MAX_LENGTH) return false
  return layers.none {
    it.id != excludingLayerId &&
      normalizedLayerName(it.name).equals(normalized, ignoreCase = true)
  }
}
