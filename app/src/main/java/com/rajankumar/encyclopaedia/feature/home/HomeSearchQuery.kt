package com.rajankumar.encyclopaedia.feature.home

data class HomeSearchQuery(val value: String) {
  val normalized: String get() = value.trim().replace(Regex("\\s+"), " ")
  val active: Boolean get() = normalized.isNotEmpty()
}
