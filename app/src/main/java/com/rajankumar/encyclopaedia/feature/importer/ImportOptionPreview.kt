package com.rajankumar.encyclopaedia.feature.importer

data class ImportOptionPreview(val label: String, val text: String)

fun EditableImportDraft.optionPreview(): List<ImportOptionPreview> = cleanedOptions.mapIndexed { index, text ->
  ImportOptionPreview(ocrOptionLabel(index), text)
}
