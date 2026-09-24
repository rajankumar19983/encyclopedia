package com.rajankumar.encyclopaedia.feature.home

data class HomeUpcomingItem(val title: String, val dateLabel: String, val detail: String? = null) {
  val displayTitle: String get() = title.trim().ifBlank { "Untitled study task" }
}
