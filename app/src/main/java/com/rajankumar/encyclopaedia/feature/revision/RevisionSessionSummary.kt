package com.rajankumar.encyclopaedia.feature.revision

data class RevisionSessionSummary(val due: Int, val weakTopics: Int, val progress: RevisionSessionProgress) {
  fun label(): String = "${revisionDueLabel(due)} • ${revisionTopicCountLabel(weakTopics)} • ${progress.label()}"
}
