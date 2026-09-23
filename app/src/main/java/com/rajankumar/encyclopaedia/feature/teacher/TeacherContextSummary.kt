package com.rajankumar.encyclopaedia.feature.teacher

data class TeacherContextSummary(val label: String, val detail: String)

fun TeacherContext?.summary(): TeacherContextSummary = when (this) {
  null -> TeacherContextSummary("General study help", "No screen-specific study context is attached.")
  else -> TeacherContextSummary(
    label = title?.takeIf { it.isNotBlank() } ?: screen.replaceFirstChar { it.uppercase() },
    detail = "Using structured context from $screen. Screenshots and source images are not required.",
  )
}
