package com.rajankumar.encyclopaedia.feature.notes.render

/**
 * Lightweight v1 parser focused on stable block boundaries and safe fallback.
 * Inline Markdown styling is intentionally left to the renderer adapter.
 */
object StudyNoteParser {
  fun parse(source: String): StudyDocument {
    val normalized = source.replace("\r\n", "\n")
    val (metadata, body) = parseFrontMatter(normalized)
    return StudyDocument(metadata, parseBlocks(body))
  }

  private fun parseFrontMatter(source: String): Pair<StudyDocumentMetadata, String> {
    if (!source.startsWith("---\n")) return StudyDocumentMetadata() to source
    val end = source.indexOf("\n---\n", startIndex = 4)
    if (end < 0) return StudyDocumentMetadata() to source

    val raw = source.substring(4, end)
    val values = raw.lines().mapNotNull { line ->
      if (!line.contains(':')) null
      else line.substringBefore(':').trim() to line.substringAfter(':').trim()
    }.toMap()

    val known = setOf("format-version", "module", "chapter", "exams", "status")
    val metadata = StudyDocumentMetadata(
      formatVersion = values["format-version"]?.toIntOrNull() ?: 1,
      module = values["module"],
      chapter = values["chapter"],
      exams = parseList(values["exams"]),
      status = values["status"],
      extra = values.filterKeys { it !in known }
    )
    return metadata to source.substring(end + 5)
  }

  private fun parseBlocks(source: String): List<StudyBlock> {
    val lines = source.lines()
    val result = mutableListOf<StudyBlock>()
    val paragraph = mutableListOf<String>()
    var index = 0

    fun flushParagraph() {
      if (paragraph.isNotEmpty()) {
        result += StudyBlock.Paragraph(paragraph.joinToString("\n").trim())
        paragraph.clear()
      }
    }

    while (index < lines.size) {
      val line = lines[index]
      when {
        line.isBlank() -> flushParagraph()
        line.matches(Regex("^#{1,6}\\s+.*")) -> {
          flushParagraph()
          val level = line.takeWhile { it == '#' }.length
          result += StudyBlock.Heading(level, line.drop(level).trim())
        }
        line.trim() == "---" -> {
          flushParagraph()
          result += StudyBlock.Divider
        }
        line.startsWith("```") -> {
          flushParagraph()
          val language = line.removePrefix("```").trim().ifBlank { null }
          val content = mutableListOf<String>()
          index++
          while (index < lines.size && !lines[index].startsWith("```")) {
            content += lines[index]
            index++
          }
          val code = content.joinToString("\n")
          result += if (language == "mermaid") StudyBlock.Mermaid(code)
          else StudyBlock.Code(language, code)
        }
        line.startsWith(":::") -> {
          flushParagraph()
          val opening = line.removePrefix(":::").trim()
          val name = opening.substringBefore(' ').trim()
          val attrs = parseAttributes(opening.substringAfter(' ', ""))
          val content = mutableListOf<String>()
          index++
          while (index < lines.size && lines[index].trim() != ":::") {
            content += lines[index]
            index++
          }
          result += StudyDirectiveParser.parse(name, attrs, content.joinToString("\n"))
        }
        line.startsWith("> ") -> {
          flushParagraph()
          val quote = mutableListOf<String>()
          while (index < lines.size && lines[index].startsWith("> ")) {
            quote += lines[index].removePrefix("> ")
            index++
          }
          index--
          result += StudyBlock.Quote(quote.joinToString("\n"))
        }
        line.matches(Regex("^\\s*[-*+]\\s+.+")) -> {
          flushParagraph()
          val items = mutableListOf<String>()
          while (index < lines.size && lines[index].matches(Regex("^\\s*[-*+]\\s+.+"))) {
            items += lines[index].replaceFirst(Regex("^\\s*[-*+]\\s+"), "")
            index++
          }
          index--
          result += StudyBlock.BulletList(items, ordered = false)
        }
        line.matches(Regex("^\\s*\\d+[.)]\\s+.+")) -> {
          flushParagraph()
          val items = mutableListOf<String>()
          while (index < lines.size && lines[index].matches(Regex("^\\s*\\d+[.)]\\s+.+"))) {
            items += lines[index].replaceFirst(Regex("^\\s*\\d+[.)]\\s+"), "")
            index++
          }
          index--
          result += StudyBlock.BulletList(items, ordered = true)
        }
        else -> paragraph += line
      }
      index++
    }
    flushParagraph()
    return result
  }

  private fun parseAttributes(raw: String): Map<String, String> {
    return Regex("([A-Za-z0-9_-]+)=\\\"([^\\\"]*)\\\"")
      .findAll(raw)
      .associate { it.groupValues[1] to it.groupValues[2] }
  }

  private fun parseList(raw: String?): List<String> = raw.orEmpty()
    .trim().removePrefix("[").removeSuffix("]")
    .split(',').map { it.trim() }.filter { it.isNotBlank() }
}
