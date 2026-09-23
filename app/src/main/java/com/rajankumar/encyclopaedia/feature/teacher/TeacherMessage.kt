package com.rajankumar.encyclopaedia.feature.teacher

import java.util.UUID

enum class TeacherMessageRole { STUDENT, TEACHER }

data class TeacherMessage(
  val id: String = UUID.randomUUID().toString(),
  val role: TeacherMessageRole,
  val text: String
)
