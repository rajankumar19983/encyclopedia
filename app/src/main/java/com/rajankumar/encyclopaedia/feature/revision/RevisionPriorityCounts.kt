package com.rajankumar.encyclopaedia.feature.revision

data class RevisionPriorityCounts(val urgent: Int, val high: Int, val normal: Int, val low: Int) {
  val total: Int get() = urgent + high + normal + low
  val needsImmediateAttention: Boolean get() = urgent > 0
}

fun List<RevisionItem>.priorityCounts() = RevisionPriorityCounts(
  count { it.priority == RevisionPriority.URGENT }, count { it.priority == RevisionPriority.HIGH },
  count { it.priority == RevisionPriority.NORMAL }, count { it.priority == RevisionPriority.LOW }
)
