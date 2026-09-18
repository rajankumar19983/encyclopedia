package com.rajankumar.encyclopaedia.feature.importer

data class ParsedQuestionDraft(
  val questionText: String,
  val options: List<String>,
  val correctAnswer: String? = null,
  val explanation: String? = null,
  val warnings: List<String> = emptyList()
)

/**
 * Conservative parser for printed MCQs.
 *
 * The parser prefers rejecting/flagging ambiguous OCR over inventing structure.
 * Every returned item is still a review draft and must be approved by the user.
 */
object OcrQuestionParser {
  private val questionStart = Regex(
    "^\\s*(?:Q(?:uestion)?\\s*)?(\\d{1,4})\\s*[.)\\-:]\\s*(.+)$",
    RegexOption.IGNORE_CASE
  )
  private val optionStart = Regex("^\\s*[(]?([A-Fa-f])[).:\\-]\\s*(.+)$")
  private val numericOptionStart = Regex("^\\s*[(]?([1-6])[).:\\-]\\s*(.+)$")
  private val answerLine = Regex(
    "^\\s*(?:ans(?:wer)?|correct\\s*(?:answer|option))\\s*[:.\\-]?\\s*[(]?([A-Fa-f1-6])[)]?.*$",
    RegexOption.IGNORE_CASE
  )
  private val explanationStart = Regex(
    "^\\s*(?:explanation|solution|reason)\\s*[:.\\-]?\\s*(.*)$",
    RegexOption.IGNORE_CASE
  )

  fun parse(rawText: String): List<ParsedQuestionDraft> {
    val lines = rawText.lines().map(::normalize).filter { it.isNotBlank() }
    if (lines.isEmpty()) return emptyList()

    val chunks = mutableListOf<MutableList<String>>()
    var current: MutableList<String>? = null
    var previousNumber: Int? = null

    for (line in lines) {
      val start = questionStart.matchEntire(line)
      if (start != null) {
        val number = start.groupValues[1].toIntOrNull()
        // A new numbered line is accepted as a boundary. Number jumps are retained
        // but later flagged; exam pages commonly start at arbitrary question numbers.
        current = mutableListOf(line)
        chunks += current
        previousNumber = number ?: previousNumber
      } else if (current != null) {
        current += line
      }
    }

    if (chunks.isEmpty()) return emptyList()

    val parsed = chunks.mapNotNull(::parseChunk)
    return removeExactDuplicates(parsed)
  }

  private fun parseChunk(lines: List<String>): ParsedQuestionDraft? {
    val first = lines.firstOrNull()?.let(questionStart::matchEntire) ?: return null
    val stemParts = mutableListOf(first.groupValues[2])
    val options = mutableListOf<String>()
    val optionLabels = mutableListOf<Char>()
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
        explanationMatch.groupValues[1].takeIf { it.isNotBlank() }?.let(explanation::add)
        continue
      }

      if (inExplanation) {
        explanation += line
        continue
      }

      val letterOption = optionStart.matchEntire(line)
      val numberOption = numericOptionStart.matchEntire(line)
      when {
        letterOption != null -> {
          optionLabels += letterOption.groupValues[1].uppercase().first()
          options += letterOption.groupValues[2].trim()
          currentOption = options.lastIndex
        }
        numberOption != null -> {
          val number = numberOption.groupValues[1].toInt()
          optionLabels += ('A'.code + number - 1).toChar()
          options += numberOption.groupValues[2].trim()
          currentOption = options.lastIndex
        }
        currentOption >= 0 -> options[currentOption] = join(options[currentOption], line)
        else -> stemParts += line
      }
    }

    val question = stemParts.joinToString(" ").trim()
    if (question.isBlank()) return null

    val safeOptions = options.take(6)
    val warnings = buildList {
      if (options.size !in 2..6) add("Expected 2–6 printed options; found ${options.size}.")
      if (options.size > 6) add("More than 6 option-like lines were detected; only the first 6 are shown.")
      if (safeOptions.any { it.length < 1 }) add("One or more options appear empty.")
      if (safeOptions.map(::fingerprint).distinct().size != safeOptions.size) add("Duplicate option text detected; verify OCR.")
      if (optionLabels.isNotEmpty() && optionLabels.take(6) != ('A'..'F').take(optionLabels.take(6).size)) {
        add("Option labels are not sequential; verify that OCR did not miss an option.")
      }
      if (answer == null) add("Correct answer was not confidently detected.")
      if (answer != null && answer.first() !in ('A'.code until ('A'.code + safeOptions.size)).map(Int::toChar)) {
        add("Detected answer does not point to one of the extracted options.")
      }
      if (question.length < 8) add("Question text looks unusually short; verify OCR.")
      if (question.count { it == '?' } > 2) add("Question contains several '?' characters; OCR may have confused symbols.")
    }

    return ParsedQuestionDraft(
      questionText = question,
      options = safeOptions,
      correctAnswer = answer,
      explanation = explanation.joinToString(" ").trim().ifBlank { null },
      warnings = warnings.distinct()
    )
  }

  private fun normalizeAnswer(value: String): String {
    val token = value.uppercase().first()
    return if (token in '1'..'6') ('A'.code + (token - '1')).toChar().toString() else token.toString()
  }

  private fun removeExactDuplicates(items: List<ParsedQuestionDraft>): List<ParsedQuestionDraft> {
    val seen = mutableSetOf<String>()
    return items.filter { seen.add(fingerprint(it.questionText) + "|" + it.options.joinToString("|") { option -> fingerprint(option) }) }
  }

  private fun fingerprint(value: String): String = value
    .lowercase()
    .replace(Regex("[^\\p{L}\\p{N}]+"), "")

  private fun join(left: String, right: String): String = "$left $right".replace(Regex("\\s+"), " ").trim()

  private fun normalize(value: String): String = value
    .replace('\u00A0', ' ')
    .replace('“', '"')
    .replace('”', '"')
    .replace('’', '\'')
    .replace(Regex("[ \\t]+"), " ")
    .trim()
}
