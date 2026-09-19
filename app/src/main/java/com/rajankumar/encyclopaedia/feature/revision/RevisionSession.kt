package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSession(
  val items: List<RevisionItem>,
  val limit: Int = RevisionConstants.defaultSessionSize
) {
  private val selectedItems: List<RevisionItem>
    get() = if (limit <= 0) emptyList() else items.take(limit.coerceAtMost(50))

  val questions get() = selectedItems.map { it.question }
  val size get() = selectedItems.size
  val isEmpty get() = selectedItems.isEmpty()
  val priorities get() = selectedItems.map { it.priority }
}
