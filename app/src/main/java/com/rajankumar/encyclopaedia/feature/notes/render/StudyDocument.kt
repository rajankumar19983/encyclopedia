package com.rajankumar.encyclopaedia.feature.notes.render

/**
 * Renderer-independent representation of a study note.
 *
 * Keep this model free of Compose types so parsing, validation, importing,
 * accessibility projection, and tests can share it.
 */
data class StudyDocument(
  val metadata: StudyDocumentMetadata = StudyDocumentMetadata(),
  val blocks: List<StudyBlock> = emptyList()
)

data class StudyDocumentMetadata(
  val formatVersion: Int = 1,
  val module: String? = null,
  val chapter: String? = null,
  val exams: List<String> = emptyList(),
  val status: String? = null,
  val extra: Map<String, String> = emptyMap()
)

sealed interface StudyBlock {
  data class Heading(val level: Int, val text: String) : StudyBlock
  data class Paragraph(val markdown: String) : StudyBlock
  data class BulletList(val items: List<String>, val ordered: Boolean = false) : StudyBlock
  data class Code(val language: String?, val source: String) : StudyBlock
  data class Quote(val markdown: String) : StudyBlock
  data object Divider : StudyBlock
  data class Table(val header: List<String>, val rows: List<List<String>>) : StudyBlock
  data class Image(val source: String, val alt: String, val caption: String? = null) : StudyBlock
  data class Math(val source: String, val display: Boolean) : StudyBlock
  data class Mermaid(val source: String) : StudyBlock
  data class Callout(val kind: CalloutKind, val markdown: String) : StudyBlock
  data class Definition(val term: String, val markdown: String) : StudyBlock
  data class Reveal(val title: String, val blocks: List<StudyBlock>) : StudyBlock
  data class ExamMetadata(
    val exams: List<String>,
    val topics: List<String>,
    val difficulty: String?
  ) : StudyBlock
  data class Mcq(
    val id: String?,
    val question: String,
    val options: List<String>,
    val answerIndex: Int?,
    val explanation: String?,
    val tags: List<String>
  ) : StudyBlock
  data class Unsupported(val kind: String?, val source: String, val reason: String? = null) : StudyBlock
}

enum class CalloutKind {
  NOTE,
  TIP,
  WARNING,
  EXAM_TRAP,
  REMEMBER
}
