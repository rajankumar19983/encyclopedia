package com.rajankumar.encyclopaedia.feature.home

data class HomeSearchQuery(val value: String) {
  val normalized: String get() = value.trim().replace(Regex("\\s+"), " ").take(120)
  val active: Boolean get() = normalized.length >= 2
  val canClear: Boolean get() = value.isNotBlank()
  val accessibilityLabel: String get() = if (active) "Search for $normalized" else "Search topics, questions, and notes"
}
