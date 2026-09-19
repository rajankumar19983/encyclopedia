package com.rajankumar.encyclopaedia.feature.revision

data class RevisionBadge(val count: Int, val hasUrgent: Boolean)

fun List<RevisionItem>.revisionBadge() = RevisionBadge(size, any { it.priority == RevisionPriority.URGENT })
