package com.rajankumar.encyclopaedia.feature.importer

data class OcrMetadataConflict(
  val field: String,
  val values: Set<String>
)

fun detectMetadataConflicts(pages: List<OcrPageText>): List<OcrMetadataConflict> {
  val metadata = pages.filter { it.error == null }.map { OcrSourceMetadataExtractor.extract(it.text) }
  fun conflict(field: String, values: List<String?>): OcrMetadataConflict? {
    val distinct = values.filterNotNull().toSet()
    return if (distinct.size > 1) OcrMetadataConflict(field, distinct) else null
  }
  return listOfNotNull(
    conflict("exam", metadata.map { it.examName }),
    conflict("year", metadata.map { it.year?.toString() }),
    conflict("date", metadata.map { it.examDate }),
    conflict("shift", metadata.map { it.shift })
  )
}
