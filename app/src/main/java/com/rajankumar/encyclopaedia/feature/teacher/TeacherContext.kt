package com.rajankumar.encyclopaedia.feature.teacher

/**
 * Describes the study material currently visible to the learner.
 * The AI teacher can use this context without requiring screenshots or
 * storing source images.
 */
data class TeacherContext(
  val screen: String,
  val title: String? = null,
  val primaryContent: String,
  val supportingContent: List<String> = emptyList()
) {
  fun asPromptContext(): String = buildList {
    add("Current screen: ${screen.trim()}")
    title?.trim()?.takeIf(String::isNotEmpty)?.let { add("Title: $it") }
    primaryContent.trim().takeIf(String::isNotEmpty)?.let { add("Content: $it") }
    supportingContent
      .map(String::trim)
      .filter(String::isNotEmpty)
      .forEach { add("Supporting content: $it") }
  }.joinToString("\n")
}
