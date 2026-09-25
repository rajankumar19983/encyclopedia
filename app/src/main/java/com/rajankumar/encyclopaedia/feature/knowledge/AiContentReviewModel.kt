package com.rajankumar.encyclopaedia.feature.knowledge

data class AiContentReviewStats(
  val nodeCount: Int,
  val lessonCount: Int,
  val maxDepth: Int,
) {
  val levelCount: Int
    get() = maxDepth + 1
}

internal sealed interface AiReviewRow {
  val key: String

  data class Node(
    val path: List<Int>,
    val depth: Int,
    val node: AiKnowledgeDraft,
  ) : AiReviewRow {
    val collapseKey: String = reviewNodeKey(path)
    override val key: String = "node:$collapseKey"
  }

  data class Lesson(
    val nodePath: List<Int>,
    val lessonIndex: Int,
    val depth: Int,
    val lesson: AiLessonDraft,
  ) : AiReviewRow {
    override val key: String = "lesson:${reviewNodeKey(nodePath)}:$lessonIndex"
  }
}

internal fun AiKnowledgeDraft.reviewStats(): AiContentReviewStats {
  var nodes = 0
  var lessons = 0
  var deepest = 0

  fun visit(node: AiKnowledgeDraft, depth: Int) {
    nodes += 1
    lessons += node.lessons.size
    deepest = maxOf(deepest, depth)
    node.children.forEach { child -> visit(child, depth + 1) }
  }

  visit(this, 0)
  return AiContentReviewStats(
    nodeCount = nodes,
    lessonCount = lessons,
    maxDepth = deepest,
  )
}

internal fun AiKnowledgeDraft.reviewRows(
  collapsedNodeKeys: Set<String> = emptySet(),
): List<AiReviewRow> = buildList {
  fun visit(node: AiKnowledgeDraft, path: List<Int>, depth: Int) {
    val nodeRow = AiReviewRow.Node(path, depth, node)
    add(nodeRow)
    if (nodeRow.collapseKey in collapsedNodeKeys) return

    node.lessons.forEachIndexed { index, lesson ->
      add(AiReviewRow.Lesson(path, index, depth, lesson))
    }
    node.children.forEachIndexed { index, child ->
      visit(child, path + index, depth + 1)
    }
  }

  visit(this@reviewRows, emptyList(), 0)
}

internal fun AiKnowledgeDraft.collapsibleNodeKeys(): Set<String> = buildSet {
  fun visit(node: AiKnowledgeDraft, path: List<Int>) {
    if (node.lessons.isNotEmpty() || node.children.isNotEmpty()) {
      add(reviewNodeKey(path))
    }
    node.children.forEachIndexed { index, child -> visit(child, path + index) }
  }

  visit(this@collapsibleNodeKeys, emptyList())
}

internal fun reviewNodeKey(path: List<Int>): String =
  if (path.isEmpty()) "root" else path.joinToString(".")
