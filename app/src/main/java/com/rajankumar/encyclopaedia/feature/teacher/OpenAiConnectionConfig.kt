package com.rajankumar.encyclopaedia.feature.teacher

/**
 * Non-secret OpenAI preferences. The API key itself must be stored separately
 * using Android-backed secure credential storage and must never enter Room,
 * backups, logs, analytics, or exported app data.
 */
data class OpenAiConnectionConfig(
  val modelPreference: String = AUTOMATIC_MODEL,
  val responseDepth: TeacherResponseDepth = TeacherResponseDepth.DETAILED
) {
  companion object {
    const val AUTOMATIC_MODEL = "automatic"
  }
}

enum class TeacherResponseDepth(val label: String) {
  CONCISE("Concise"),
  DETAILED("Detailed"),
  VERY_DETAILED("Very detailed")
}
