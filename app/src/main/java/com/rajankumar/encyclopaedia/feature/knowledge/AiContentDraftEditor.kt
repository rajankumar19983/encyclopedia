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

  fun updateNodeAtPath(
    root: AiKnowledgeDraft,
    path: List<Int>,
    transform: (AiKnowledgeDraft) -> AiKnowledgeDraft,
  ): AiKnowledgeDraft {
    if (path.isEmpty()) return transform(root)
    val childIndex = path.first()
    require(childIndex in root.children.indices) { "Invalid knowledge draft path: $path" }
    val updatedChild = updateNodeAtPath(root.children[childIndex], path.drop(1), transform)
    return root.copy(
      children = root.children.mapIndexed { index, child ->
        if (index == childIndex) updatedChild else child
      },
    )
  }

  fun updateLessonAtPath(
    root: AiKnowledgeDraft,
    nodePath: List<Int>,
    lessonIndex: Int,
    transform: (AiLessonDraft) -> AiLessonDraft,
  ): AiKnowledgeDraft = updateNodeAtPath(root, nodePath) { node ->
    require(lessonIndex in node.lessons.indices) {
      "Invalid lesson index $lessonIndex at knowledge draft path $nodePath"
    }
    node.copy(
      lessons = node.lessons.mapIndexed { index, lesson ->
        if (index == lessonIndex) transform(lesson) else lesson
      },
    )
  }

  fun removeNodeAtPath(root: AiKnowledgeDraft, path: List<Int>): AiKnowledgeDraft {
    require(path.isNotEmpty()) { "The root knowledge draft cannot be removed." }
    val parentPath = path.dropLast(1)
    val childIndex = path.last()
    return updateNodeAtPath(root, parentPath) { parent ->
      require(childIndex in parent.children.indices) { "Invalid knowledge draft path: $path" }
      removeChild(parent, childIndex)
    }
  }

  fun removeLessonAtPath(
    root: AiKnowledgeDraft,
    nodePath: List<Int>,
    lessonIndex: Int,
  ): AiKnowledgeDraft = updateNodeAtPath(root, nodePath) { node ->
    require(lessonIndex in node.lessons.indices) {
      "Invalid lesson index $lessonIndex at knowledge draft path $nodePath"
    }
    removeLesson(node, lessonIndex)
  }
}
