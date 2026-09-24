package com.rajankumar.encyclopaedia.feature.knowledge

data class AiContentValidation(
  val isValid: Boolean,
  val errors: List<String>
)

object AiContentValidator {
  private const val MAX_DEPTH = 6
  private const val MAX_NODES = 250

  fun validate(proposal: AiContentProposal): AiContentValidation {
    val errors = mutableListOf<String>()
    var nodes = 0

    fun visit(node: AiKnowledgeDraft, depth: Int) {
      nodes += 1
      if (node.title.isBlank()) errors += "A knowledge node has an empty title."
      if (depth > MAX_DEPTH) errors += "Knowledge hierarchy exceeds maximum depth of $MAX_DEPTH."
      node.lessons.forEach { lesson ->
        if (lesson.title.isBlank()) errors += "A lesson has an empty title."
        if (lesson.content.isBlank()) errors += "Lesson '${lesson.title}' has empty content."
      }
      node.children.forEach { visit(it, depth + 1) }
    }

    visit(proposal.root, 1)
    if (nodes > MAX_NODES) errors += "Proposal contains $nodes nodes; maximum is $MAX_NODES."
    return AiContentValidation(errors.isEmpty(), errors.distinct())
  }
}
