package com.rajankumar.encyclopaedia.feature.performance

data class RevisionMilestone(val prioritization: Boolean, val scoring: Boolean, val boundedSessions: Boolean, val summaries: Boolean, val accessibility: Boolean) {
  val complete: Boolean get() = prioritization && scoring && boundedSessions && summaries && accessibility
}

val revisionMilestone = RevisionMilestone(true, true, true, true, true)
