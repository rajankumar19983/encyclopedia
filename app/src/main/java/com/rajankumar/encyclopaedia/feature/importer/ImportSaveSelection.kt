package com.rajankumar.encyclopaedia.feature.importer

data class ImportSaveSelection(
  val selectedIds: Set<String> = emptySet(),
) {
  fun toggle(id: String): ImportSaveSelection = copy(
    selectedIds = if (id in selectedIds) selectedIds - id else selectedIds + id
  )

  fun contains(id: String): Boolean = id in selectedIds
}
