package com.rajankumar.encyclopaedia.feature.performance

data class RevisionSummary(val total: Int, val high: Int, val medium: Int, val low: Int)

fun revisionSummary(items: List<RevisionQueueItem>) = RevisionSummary(items.size, items.count { it.priority == RevisionPriority.HIGH }, items.count { it.priority == RevisionPriority.MEDIUM }, items.count { it.priority == RevisionPriority.LOW })
