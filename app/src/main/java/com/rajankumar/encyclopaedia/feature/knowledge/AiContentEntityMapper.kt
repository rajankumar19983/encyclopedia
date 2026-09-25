package com.rajankumar.encyclopaedia.feature.knowledge

import com.rajankumar.encyclopaedia.data.local.KnowledgeContentSource
import com.rajankumar.encyclopaedia.data.local.KnowledgeNodeEntity
import com.rajankumar.encyclopaedia.data.local.LessonEntity
import java.util.UUID

data class ApprovedKnowledgeBatch(
  val nodes: List<KnowledgeNodeEntity>,
  val lessons: List<LessonEntity>
)

object AiContentEntityMapper {
  fun map(
    proposal: AiContentProposal,
    parentNodeId: String? = null,
    idFactory: () -> String = { UUID.randomUUID().toString() },
    now: Long = System.currentTimeMillis()
  ): ApprovedKnowledgeBatch {
    val nodes = mutableListOf<KnowledgeNodeEntity>()
    val lessons = mutableListOf<LessonEntity>()

    fun visit(draft: AiKnowledgeDraft, parentId: String?, sortOrder: Int) {
      val nodeId = idFactory()
      nodes += KnowledgeNodeEntity(
        id = nodeId,
        parentId = parentId,
        name = draft.title.trim(),
        description = draft.description?.trim()?.ifBlank { null },
        sortOrder = sortOrder,
        source = KnowledgeContentSource.AI,
        createdAt = now,
        updatedAt = now
      )
      draft.lessons.forEachIndexed { index, lesson ->
        lessons += LessonEntity(
          id = idFactory(),
          knowledgeNodeId = nodeId,
          title = lesson.title.trim(),
          content = lesson.content.trim(),
          sortOrder = index,
          source = KnowledgeContentSource.AI,
          createdAt = now,
          updatedAt = now
        )
      }
      draft.children.forEachIndexed { index, child -> visit(child, nodeId, index) }
    }

    visit(proposal.root, parentNodeId, 0)
    return ApprovedKnowledgeBatch(nodes, lessons)
  }
}
