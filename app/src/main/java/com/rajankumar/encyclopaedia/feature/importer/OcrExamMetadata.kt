package com.rajankumar.encyclopaedia.feature.importer

private val examPatterns = listOf(
  Regex("(?i)\\b(DSSSB(?:\\s+TGT)?(?:\\s+Computer\\s+Science)?)\\b"),
  Regex("(?i)\\b(BPSC(?:\\s+TRE(?:\\s*\\d+(?:\\.\\d+)?)?)?)\\b"),
  Regex("(?i)\\b(CTET|UGC\\s*NET|GATE)\\b")
)

data class OcrExamMetadata(val examName: String?)

fun detectOcrExamMetadata(rawText: String): OcrExamMetadata {
  val english = OcrEnglishTextFilter.filter(rawText)
  val match = examPatterns.firstNotNullOfOrNull { it.find(english)?.groupValues?.get(1) }
  return OcrExamMetadata(match?.replace(Regex("\\s+"), " ")?.trim())
}
