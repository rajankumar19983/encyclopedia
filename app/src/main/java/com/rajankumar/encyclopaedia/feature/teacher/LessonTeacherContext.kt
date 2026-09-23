package com.rajankumar.encyclopaedia.feature.teacher

import com.rajankumar.encyclopaedia.data.local.LessonEntity

fun LessonEntity.asTeacherContext(topicName: String? = null): TeacherContext = TeacherContext(
  screen = "Knowledge & Lessons",
  title = title,
  primaryContent = content,
  supportingContent = listOfNotNull(
    topicName?.trim()?.takeIf(String::isNotEmpty)?.let { "Topic: $it" }
  )
)
