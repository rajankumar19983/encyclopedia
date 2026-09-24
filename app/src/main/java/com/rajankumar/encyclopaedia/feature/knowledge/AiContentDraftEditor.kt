package com.rajankumar.encyclopaedia.feature.knowledge

object AiContentDraftEditor {
  fun rename(node: AiKnowledgeDraft, title: String): AiKnowledgeDraft =
    node.copy(title = title.trim())

  fun updateDescription(node: AiKnowledgeDraft, description: String): AiKnowledgeDraft =
    node.copy(description = description.trim().ifBlank { null })

  fun addChild(parent: AiKnowledgeDraft, child: AiKnowledgeDraft): AiKnowledgeDraft =
    parent.copy(children = parent.children + child)

  fun removeChild(parent: AiKnowledgeDraft, index: Int): AiKnowledgeDraft =
    parent.copy(children = parent.children.filterIndexed { childIndex, _ -> childIndex != index })

  fun addLesson(node: AiKnowledgeDraft, lesson: AiLessonDraft): AiKnowledgeDraft =
    node.copy(lessons = node.lessons + lesson)

  fun removeLesson(node: AiKnowledgeDraft, index: Int): AiKnowledgeDraft =
    node.copy(lessons = node.lessons.filterIndexed { lessonIndex, _ -> lessonIndex != index })
}
