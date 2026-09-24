package com.rajankumar.encyclopaedia.feature.importer

data class OcrSourceMetadata(
  val examName: String? = null,
  val year: Int? = null,
  val examDate: String? = null,
  val shift: String? = null,
  val confidence: OcrMetadataConfidence = OcrMetadataConfidence.NONE
)

enum class OcrMetadataConfidence { NONE, LOW, MEDIUM, HIGH }

object OcrSourceMetadataExtractor {
  private val examPatterns = listOf(
    "DSSSB" to Regex("\\b(?:DSSSB|DELHI\\s+SUBORDINATE\\s+SERVICES\\s+SELECTION\\s+BOARD)\\b", RegexOption.IGNORE_CASE),
    "BPSC" to Regex("\\b(?:BPSC|BIHAR\\s+PUBLIC\\s+SERVICE\\s+COMMISSION)\\b", RegexOption.IGNORE_CASE),
    "CTET" to Regex("\\b(?:CTET|CENTRAL\\s+TEACHER\\s+ELIGIBILITY\\s+TEST)\\b", RegexOption.IGNORE_CASE),
    "UGC NET" to Regex("\\b(?:UGC[ -]*NET|NATIONAL\\s+ELIGIBILITY\\s+TEST)\\b", RegexOption.IGNORE_CASE),
    "GATE" to Regex("\\bGATE(?:\\s+EXAM(?:INATION)?)?\\b", RegexOption.IGNORE_CASE),
    "NIELIT" to Regex("\\bNIELIT\\b", RegexOption.IGNORE_CASE),
    "KVS" to Regex("\\b(?:KVS|KENDRIYA\\s+VIDYALAYA\\s+SANGATHAN)\\b", RegexOption.IGNORE_CASE),
    "NVS" to Regex("\\b(?:NVS|NAVODAYA\\s+VIDYALAYA\\s+SAMITI)\\b", RegexOption.IGNORE_CASE)
  )
  private val year = Regex("\\b(19\\d{2}|20\\d{2})\\b")
  private val date = Regex("\\b([0-3]?\\d)[./-]([01]?\\d)[./-]((?:19|20)\\d{2})\\b")
  private val shift = Regex("\\b(?:SHIFT|SITTING|SESSION)\\s*[-:]?\\s*(I{1,3}|[1-3]|MORNING|AFTERNOON|EVENING)\\b", RegexOption.IGNORE_CASE)

  fun extract(text: String): OcrSourceMetadata {
    val normalized = text.replace(Regex("[ \\t]+"), " ")
    val exam = examPatterns.firstOrNull { (_, pattern) -> pattern.containsMatchIn(normalized) }?.first
    val dateMatch = date.find(normalized)
    val examDate = dateMatch?.let { match ->
      val day = match.groupValues[1].padStart(2, '0')
      val month = match.groupValues[2].padStart(2, '0')
      "${match.groupValues[3]}-$month-$day"
    }
    val parsedYear = examDate?.take(4)?.toIntOrNull() ?: year.find(normalized)?.value?.toIntOrNull()
    val parsedShift = shift.find(normalized)?.groupValues?.get(1)?.let(::normalizeShift)
    val evidence = listOf(exam, parsedYear, examDate, parsedShift).count { it != null }
    val confidence = when {
      exam != null && evidence >= 3 -> OcrMetadataConfidence.HIGH
      exam != null && evidence >= 2 -> OcrMetadataConfidence.MEDIUM
      evidence > 0 -> OcrMetadataConfidence.LOW
      else -> OcrMetadataConfidence.NONE
    }
    return OcrSourceMetadata(exam, parsedYear, examDate, parsedShift, confidence)
  }

  private fun normalizeShift(value: String): String = when (value.uppercase()) {
    "I", "1" -> "SHIFT 1"
    "II", "2" -> "SHIFT 2"
    "III", "3" -> "SHIFT 3"
    "MORNING" -> "MORNING"
    "AFTERNOON" -> "AFTERNOON"
    "EVENING" -> "EVENING"
    else -> value.uppercase()
  }
}
