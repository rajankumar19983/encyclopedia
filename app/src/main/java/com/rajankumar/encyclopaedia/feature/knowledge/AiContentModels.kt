package com.rajankumar.encyclopaedia.feature.knowledge

data class AiContentRequest(
  val topic: String,
  val parentNodeId: String? = null,
  val depth: AiContentDepth = AiContentDepth.STANDARD,
  val includeLessons: Boolean = true
)

enum class AiContentDepth {
  QUICK,
  STANDARD,
  DEEP
}

data class AiKnowledgeDraft(
  val title: String,
  val description: String? = null,
  val lessons: List<AiLessonDraft> = emptyList(),
  val children: List<AiKnowledgeDraft> = emptyList()
)

data class AiLessonDraft(
  val title: String,
  val content: String
)

data class AiContentProposal(
  val root: AiKnowledgeDraft,
  val generatedAt: Long = System.currentTimeMillis()
)
