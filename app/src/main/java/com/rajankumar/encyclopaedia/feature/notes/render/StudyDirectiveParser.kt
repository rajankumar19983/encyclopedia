package com.rajankumar.encyclopaedia.feature.notes.render

/** Parser for Encyclopedia-specific ::: directives. */
object StudyDirectiveParser {
  fun parse(name: String, attributes: Map<String, String>, body: String): StudyBlock {
    return when (name.trim().lowercase()) {
      "note" -> StudyBlock.Callout(CalloutKind.NOTE, body.trim())
      "tip" -> StudyBlock.Callout(CalloutKind.TIP, body.trim())
      "warning" -> StudyBlock.Callout(CalloutKind.WARNING, body.trim())
      "exam-trap" -> StudyBlock.Callout(CalloutKind.EXAM_TRAP, body.trim())
      "remember" -> StudyBlock.Callout(CalloutKind.REMEMBER, body.trim())
      "definition" -> StudyBlock.Definition(
        term = attributes["term"].orEmpty(),
        markdown = body.trim()
      )
      "reveal" -> StudyBlock.Reveal(
        title = attributes["title"] ?: "Show answer",
        blocks = listOf(StudyBlock.Paragraph(body.trim()))
      )
      "exam" -> parseExam(body)
      "mcq" -> parseMcq(body)
      else -> StudyBlock.Unsupported(name, body, "Unknown study directive")
    }
  }

  private fun parseExam(body: String): StudyBlock {
    val values = keyValues(body)
    return StudyBlock.ExamMetadata(
      exams = listValue(values["exams"]),
      topics = listValue(values["topics"]),
      difficulty = values["difficulty"]
    )
  }

  private fun parseMcq(body: String): StudyBlock {
    val lines = body.lines()
    val scalar = linkedMapOf<String, String>()
    val options = mutableListOf<String>()
    var readingOptions = false

    lines.forEach { raw ->
      val line = raw.trim()
      when {
        line.startsWith("options:") -> readingOptions = true
        readingOptions && line.startsWith("- ") -> options += line.removePrefix("- ").trim()
        line.contains(":") -> {
          readingOptions = false
          val key = line.substringBefore(':').trim()
          val value = line.substringAfter(':').trim()
          scalar[key] = value
        }
      }
    }

    val question = scalar["question"]
    if (question.isNullOrBlank() || options.isEmpty()) {
      return StudyBlock.Unsupported("mcq", body, "MCQ requires a question and options")
    }

    return StudyBlock.Mcq(
      id = scalar["id"],
      question = question,
      options = options,
      answerIndex = scalar["answer"]?.toIntOrNull()?.takeIf { it in options.indices },
      explanation = scalar["explanation"],
      tags = listValue(scalar["tags"])
    )
  }

  private fun keyValues(body: String): Map<String, String> = body.lines()
    .map { it.trim() }
    .filter { it.contains(":") }
    .associate { line -> line.substringBefore(':').trim() to line.substringAfter(':').trim() }

  private fun listValue(value: String?): List<String> {
    if (value.isNullOrBlank()) return emptyList()
    return value.trim().removePrefix("[").removeSuffix("]")
      .split(',')
      .map { it.trim().trim('"', '\'') }
      .filter { it.isNotBlank() }
  }
}
