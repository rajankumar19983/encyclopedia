package com.rajankumar.encyclopaedia.feature.importer

fun OcrBatchDiagnostics.message(): String? {
  if (!needsReview) return null
  return buildList {
    if (detectedQuestionNumbers != parsedDrafts) add("Detected $detectedQuestionNumbers numbered questions but produced $parsedDrafts review drafts.")
    if (missingQuestionNumbers.isNotEmpty()) add("Missing question numbers: ${missingQuestionNumbers.joinToString()}.")
    if (duplicateQuestionNumbers.isNotEmpty()) add("Duplicate question numbers: ${duplicateQuestionNumbers.joinToString()}.")
  }.joinToString(" ")
}

data class OcrImportParseResult(
  val drafts: List<ParsedQuestionDraft>,
  val removedDevanagari: Boolean
)
fun parseEnglishOcrQuestions(rawText: String): OcrImportParseResult {
  val sanitized = sanitizeOcrImportText(rawText)
  return OcrImportParseResult(
    drafts = OcrQuestionParser.parse(sanitized.text),
    removedDevanagari = sanitized.removedDevanagari
  )
}

/** Removes Devanagari OCR content before MCQ parsing while preserving printed English structure. */
object OcrEnglishTextFilter {
  private val devanagari = Regex("[\\u0900-\\u097F]")
  private val whitespace = Regex("[ \\t]+")

  fun filter(rawText: String): String = rawText.lineSequence()
    .map(::filterLine)
    .filter { it.isNotBlank() }
    .joinToString("\n")

  fun containsDevanagari(value: String): Boolean = devanagari.containsMatchIn(value)

  private fun filterLine(line: String): String = line
    .replace(devanagari, " ")
    .replace(whitespace, " ")
    .trim()
}

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

data class OcrLanguageAudit(
  val questionHasDevanagari: Boolean,
  val optionIndexesWithDevanagari: List<Int>,
  val explanationHasDevanagari: Boolean
) {
  val clean: Boolean get() = !questionHasDevanagari && optionIndexesWithDevanagari.isEmpty() && !explanationHasDevanagari
}

fun ParsedQuestionDraft.languageAudit(): OcrLanguageAudit = OcrLanguageAudit(
  questionHasDevanagari = OcrEnglishTextFilter.containsDevanagari(questionText),
  optionIndexesWithDevanagari = options.mapIndexedNotNull { index, option -> index.takeIf { OcrEnglishTextFilter.containsDevanagari(option) } },
  explanationHasDevanagari = explanation?.let(OcrEnglishTextFilter::containsDevanagari) == true
)

object OcrLanguageFilter {
  private val devanagari = Regex("[\\u0900-\\u097F]")

  fun removeDevanagariLines(text: String): String = text
    .lines()
    .filterNot { devanagari.containsMatchIn(it) }
    .joinToString("\n")
    .trim()

  fun containsDevanagari(text: String): Boolean = devanagari.containsMatchIn(text)
}

data class OcrLineQuality(val text: String, val usable: Boolean, val reason: String? = null)

fun assessOcrLine(line: String): OcrLineQuality {
  val text = line.trim()
  if (text.isBlank()) return OcrLineQuality(text, false, "blank")
  if (OcrLanguageFilter.containsDevanagari(text)) return OcrLineQuality(text, false, "contains Devanagari")
  val visible = text.count { !it.isWhitespace() }
  val lettersOrDigits = text.count { it.isLetterOrDigit() }
  if (visible >= 4 && lettersOrDigits * 100 / visible < 35) return OcrLineQuality(text, false, "mostly OCR noise")
  return OcrLineQuality(text, true)
}

enum class OcrQuestionLengthQuality { TOO_SHORT, NORMAL, VERY_LONG }

fun classifyOcrQuestionLength(text: String): OcrQuestionLengthQuality = when {
  text.trim().length < 5 -> OcrQuestionLengthQuality.TOO_SHORT
  text.trim().length > 2000 -> OcrQuestionLengthQuality.VERY_LONG
  else -> OcrQuestionLengthQuality.NORMAL
}

private val numberedQuestion = Regex("^\\s*(?:Q(?:uestion)?\\s*)?(\\d{1,4})\\s*[.)\\-:]", RegexOption.IGNORE_CASE)

fun extractQuestionNumbers(rawText: String): List<Int> = rawText.lineSequence()
  .mapNotNull { line -> numberedQuestion.find(line)?.groupValues?.get(1)?.toIntOrNull() }
  .toList()

data class OcrRemovedLinePreview(val text: String, val reason: String)

fun OcrSanitizationResult.removedLinePreviews(limit: Int = 8): List<OcrRemovedLinePreview> = removedLines
  .take(limit.coerceAtLeast(0))
  .map { OcrRemovedLinePreview(it.text, it.reason ?: "excluded by OCR quality checks") }

data class OcrSanitizationSummary(val removedCount: Int, val devanagariCount: Int, val noiseCount: Int)

fun OcrSanitizationResult.summary(): OcrSanitizationSummary = OcrSanitizationSummary(
  removedCount = removedLines.size,
  devanagariCount = removedLines.count { it.reason == "contains Devanagari" },
  noiseCount = removedLines.count { it.reason == "mostly OCR noise" },
)

fun OcrSanitizationSummary.message(): String? = if (removedCount == 0) {
  null
} else {
  "Excluded $removedCount suspect OCR lines from parsing • $devanagariCount Devanagari • $noiseCount noise."
}

data class OcrSanitizationResult(val text: String, val removedLines: List<OcrLineQuality>)

fun sanitizeOcrText(raw: String): OcrSanitizationResult {
  val assessed = raw.lines().map(::assessOcrLine)
  return OcrSanitizationResult(
    text = assessed.filter { it.usable }.joinToString("\n") { it.text }.trim(),
    removedLines = assessed.filter { !it.usable && it.text.isNotBlank() },
  )
}

data class OcrQuestionSequenceCheck(val numbers: List<Int>, val missing: List<Int>, val duplicates: List<Int>) {
  val needsReview: Boolean get() = missing.isNotEmpty() || duplicates.isNotEmpty()
}

fun checkQuestionSequence(numbers: List<Int>): OcrQuestionSequenceCheck {
  if (numbers.isEmpty()) return OcrQuestionSequenceCheck(emptyList(), emptyList(), emptyList())
  val duplicates = numbers.groupingBy { it }.eachCount().filterValues { it > 1 }.keys.sorted()
  val unique = numbers.distinct().sorted()
  val missing = if (unique.size < 2) emptyList() else (unique.first()..unique.last()).filterNot(unique::contains)
  return OcrQuestionSequenceCheck(numbers, missing, duplicates)
}

data class OcrTextQuality(
  val suspiciousReplacementCharacters: Int,
  val devanagariCharacters: Int,
  val hasUsableLatinText: Boolean,
) {
  val needsReview: Boolean get() = suspiciousReplacementCharacters > 0 || devanagariCharacters > 0 || !hasUsableLatinText
}

fun assessOcrTextQuality(text: String): OcrTextQuality = OcrTextQuality(
  suspiciousReplacementCharacters = text.count { it == '\uFFFD' },
  devanagariCharacters = text.count { it in '\u0900'..'\u097F' },
  hasUsableLatinText = text.any { it in 'A'..'Z' || it in 'a'..'z' },
)
