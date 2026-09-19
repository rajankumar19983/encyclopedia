package com.rajankumar.encyclopaedia.feature.accessibility

/**
 * A small, UI-independent representation of study content that can be read
 * aloud by the accessibility layer.
 */
data class SpeakableContent(
  val title: String? = null,
  val body: String,
  val supportingText: List<String> = emptyList()
) {
  fun asSpeechText(): String = buildList {
    title?.trim()?.takeIf { it.isNotEmpty() }?.let(::add)
    body.trim().takeIf { it.isNotEmpty() }?.let(::add)
    supportingText
      .map(String::trim)
      .filter(String::isNotEmpty)
      .forEach(::add)
  }.joinToString(separator = ". ")
}
