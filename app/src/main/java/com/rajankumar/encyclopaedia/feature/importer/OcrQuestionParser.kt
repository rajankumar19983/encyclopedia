package com.rajankumar.encyclopaedia.feature.importer

data class ParsedQuestionDraft(
  val questionText: String,
  val options: List<String>,
  val correctAnswer: String? = null,
  val explanation: String? = null,
  val warnings: List<String> = emptyList()
)

/**
 * Conservative parser for printed MCQ OCR text.
 *
 * It deliberately returns drafts for human review instead of silently persisting
 * uncertain OCR output. Handwriting rejection belongs to the image/layout stage;
 * this parser never tries to invent missing text or answers.
 */
object OcrQuestionParser {
  private val questionStart = Regex("^\\s*(?:Q(?:uestion)?\\s*)?(\\d{1,4})[.)\\-:]\\s+(.+)$", RegexOption.IGNORE_CASE)
  private val optionStart = Regex("^\\s*[(]?([A-Fa-f])[).:\\-]\\s*(.+)$")
  private val answerLine = Regex("^\\s*(?:ans(?:wer)?|correct\\s*answer)\\s*[:.\\-]?\\s*([A-Fa-f])(?:[).])?.*$", RegexOption.IGNORE_CASE)

  fun parse(rawText: String): List<ParsedQuestionDraft> {
    val lines = rawText.lines().map { normalize(it) }.filter { it.isNotBlank() }
    if (lines.isEmpty()) return emptyList()

    val chunks = mutableListOf<MutableList<String>>()
    var current: MutableList<String>? = null

    for (line in lines) {
      if (questionStart.matches(line)) {
        current = mutableListOf(line)
        chunks += current
      } else if (current != null) {
        current += line
      }
    }

    // Never pretend an unnumbered page was parsed reliably.
    if (chunks.isEmpty()) return emptyList()
    return chunks.mapNotNull(::parseChunk)
  }

  private fun parseChunk(lines: List<String>): ParsedQuestionDraft? {
    if (lines.isEmpty()) return null
    val first = questionStart.matchEntire(lines.first()) ?: return null
    val stemParts = mutableListOf(first.groupValues[2])
    val options = mutableListOf<String>()
    var currentOption = -1
    var answer: String? = null

    for (line in lines.drop(1)) {
      val answerMatch = answerLine.matchEntire(line)
      if (answerMatch != null) {
        answer = answerMatch.groupValues[1].uppercase()
        continue
      }

      val optionMatch = optionStart.matchEntire(line)
      if (optionMatch != null) {
        options += optionMatch.groupValues[2].trim()
        currentOption = options.lastIndex
      } else if (currentOption >= 0) {
        options[currentOption] = "${options[currentOption]} $line".trim()
      } else {
        stemParts += line
      }
    }

    val warnings = buildList {
      if (options.size !in 2..6) add("Expected 2–6 printed options; found ${options.size}.")
      if (answer == null) add("Correct answer was not confidently detected.")
      if (stemParts.joinToString(" ").length < 8) add("Question text looks unusually short; verify OCR.")
    }

    return ParsedQuestionDraft(
      questionText = stemParts.joinToString(" ").trim(),
      options = options.take(6),
      correctAnswer = answer,
      warnings = warnings
    )
  }

  private fun normalize(value: String): String = value
    .replace('\u00A0', ' ')
    .replace(Regex("[ \\t]+"), " ")
    .trim()
}
