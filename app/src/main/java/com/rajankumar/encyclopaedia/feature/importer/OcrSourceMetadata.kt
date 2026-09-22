package com.rajankumar.encyclopaedia.feature.importer

data class OcrSourceMetadata(val examName: String? = null, val year: Int? = null)

object OcrSourceMetadataExtractor {
  private val knownExam = Regex("\\b(DSSSB|BPSC|CTET|UGC\\s*NET|GATE|NIELIT)\\b", RegexOption.IGNORE_CASE)
  private val year = Regex("\\b(20\\d{2}|19\\d{2})\\b")

  fun extract(text: String): OcrSourceMetadata {
    val exam = knownExam.find(text)?.value?.uppercase()?.replace(Regex("\\s+"), " ")
    val parsedYear = year.find(text)?.value?.toIntOrNull()
    return OcrSourceMetadata(exam, parsedYear)
  }
}
