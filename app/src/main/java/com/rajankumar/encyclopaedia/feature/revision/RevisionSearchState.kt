package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSearchState(val query: String = "", val priority: RevisionPriority? = null, val sort: RevisionSort = RevisionSort.PRIORITY)

fun List<RevisionItem>.applyRevisionState(state: RevisionSearchState) = filterRevisionQueue(state.priority, state.query).sortedForRevision(state.sort)
