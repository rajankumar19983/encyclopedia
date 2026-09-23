package com.rajankumar.encyclopaedia.feature.importer

data class ParsedQuestionDraft(
  val questionText: String,
  val options: List<String>,
  val correctAnswer: String? = null,
  val explanation: String? = null,
  val warnings: List<String> = emptyList()
)

/** Conservative parser for printed MCQs. Every result remains a review draft. */
object OcrQuestionParser {
  private val questionStart = Regex("^\\s*(?:Q(?:uestion)?\\s*)?(\\d{1,4})\\s*[.)\\-:]\\s*(.+)$", RegexOption.IGNORE_CASE)
  private val optionStart = Regex("^\\s*[(]?([A-Za-z])[).:\\-]\\s*(.+)$")
  private val numericOptionStart = Regex("^\\s*[(]?(\\d{1,2})[).:\\-]\\s*(.+)$")
  private val answerLine = Regex("^\\s*(?:ans(?:wer)?|correct\\s*(?:answer|option))\\s*[:.\\-]?\\s*[(]?([A-Za-z]|\\d{1,2})[)]?.*$", RegexOption.IGNORE_CASE)
  private val explanationStart = Regex("^\\s*(?:explanation|solution|reason)\\s*[:.\\-]?\\s*(.*)$", RegexOption.IGNORE_CASE)

  fun parse(rawText: String): List<ParsedQuestionDraft> {
    val lines = rawText.lines().map(::normalize).filter { it.isNotBlank() }
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
    return removeExactDuplicates(chunks.mapNotNull(::parseChunk))
  }

  private fun parseChunk(lines: List<String>): ParsedQuestionDraft? {
    val first = lines.firstOrNull()?.let(questionStart::matchEntire) ?: return null
    val stemParts = mutableListOf(first.groupValues[2])
    val options = mutableListOf<String>()
    val optionLabels = mutableListOf<Int>()
    val explanation = mutableListOf<String>()
    var currentOption = -1
    var inExplanation = false
    var answer: String? = null

    for (line in lines.drop(1)) {
      val answerMatch = answerLine.matchEntire(line)
      if (answerMatch != null) {
        answer = normalizeAnswer(answerMatch.groupValues[1])
        currentOption = -1
        continue
      }
      val explanationMatch = explanationStart.matchEntire(line)
      if (explanationMatch != null) {
        inExplanation = true
        currentOption = -1
        val initialExplanation = explanationMatch.groupValues[1]
        if (initialExplanation.isNotBlank()) explanation += initialExplanation
        continue
      }
      if (inExplanation) {
        explanation += line
        continue
      }
      val letter = optionStart.matchEntire(line)
      val number = numericOptionStart.matchEntire(line)
      when {
        letter != null -> {
          val index = letter.groupValues[1].uppercase().first() - 'A'
          if (index in 0..25) {
            optionLabels += index
            options += letter.groupValues[2].trim()
            currentOption = options.lastIndex
          }
        }
        number != null -> {
          val index = number.groupValues[1].toIntOrNull()?.minus(1)
          if (index != null && index in 0..25) {
            optionLabels += index
            options += number.groupValues[2].trim()
            currentOption = options.lastIndex
          }
        }
        currentOption >= 0 -> options[currentOption] = join(options[currentOption], line)
        else -> stemParts += line
      }
    }

    val question = stemParts.joinToString(" ").trim()
    if (question.isBlank()) return null
    val warnings = buildList {
      if (options.size < 2) add("Expected at least 2 printed options; found ${options.size}.")
      if (options.size > 12) add("Unusually many option-like lines were detected; verify OCR structure.")
      if (options.map(::fingerprint).distinct().size != options.size) add("Duplicate option text detected; verify OCR.")
      if (optionLabels.isNotEmpty() && optionLabels != optionLabels.indices.toList()) add("Option labels are not sequential; verify that OCR did not miss an option.")
      if (answer == null) add("Correct answer was not confidently detected.")
      val answerIndex = answer?.let(::answerIndex)
      if (answerIndex != null && answerIndex !in options.indices) add("Detected answer does not point to one of the extracted options.")
      if (question.length < 8) add("Question text looks unusually short; verify OCR.")
      if (question.count { it == '?' } > 2) add("Question contains several '?' characters; OCR may have confused symbols.")
    }
    return ParsedQuestionDraft(question, options, answer, explanation.joinToString(" ").trim().ifBlank { null }, warnings.distinct())
  }

  private fun normalizeAnswer(value: String): String {
    val trimmed = value.trim()
    val number = trimmed.toIntOrNull()
    return if (number != null && number in 1..26) ('A'.code + number - 1).toChar().toString() else trimmed.uppercase().take(1)
  }

  private fun answerIndex(value: String): Int? = value.firstOrNull()?.let { it - 'A' }
  private fun removeExactDuplicates(items: List<ParsedQuestionDraft>): List<ParsedQuestionDraft> {
    val seen = mutableSetOf<String>()
    return items.filter { seen.add(fingerprint(it.questionText) + "|" + it.options.joinToString("|") { option -> fingerprint(option) }) }
  }
  private fun fingerprint(value: String): String = value.lowercase().replace(Regex("[^\\p{L}\\p{N}]+"), "")
  private fun join(left: String, right: String): String = "$left $right".replace(Regex("\\s+"), " ").trim()
  private fun normalize(value: String): String = value.replace('\u00A0', ' ').replace('“', '"').replace('”', '"').replace('’', '\'').replace(Regex("[ \\t]+"), " ").trim()
}
